package com.brave.mystery.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfoplus;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

@Service
public class DriveService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DriveService.class);
    private static final String JSON = "{\"web\":{\"client_id\":\"456405405866-2ru5bltgivtr2hntllk4bbalruss73pv.apps.googleusercontent.com\",\"project_id\":\"inlaid-plasma-117917\",\"auth_uri\":\"https://accounts.google.com/o/oauth2/auth\",\"token_uri\":\"https://accounts.google.com/o/oauth2/token\",\"auth_provider_x509_cert_url\":\"https://www.googleapis.com/oauth2/v1/certs\",\"client_secret\":\"oh_ICw2LCEbZEl5W9S9zCSUN\"}}";
    private static final String APPLICATION_NAME = "Mystery Hunter Web App";
    private static final String REDIRECT_URI = "https://mysteryhunting.herokuapp.com";
    private static final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE, "https://spreadsheets.google.com/feeds", "email", "profile");
    private static GoogleAuthorizationCodeFlow flow = null;
    private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final String SPREADSHEET_MIME = "application/vnd.google-apps.spreadsheet";
    private static final String PARENT_FOLDER = "0B72n07EhrFPsZUdWM0QzejB2eDg";
    private static final String MASTER_SHEET_ID = "1KtA6dxo7hUTTyGarHj5W4n3EAGu2oj42vdEABr-G-AQ";
    private static final String SPREADSHEET_PRE = "https://docs.google.com/spreadsheets/d/";

    private Credential savedCred = null;

    /**
     * Exception thrown when an error occurred while retrieving credentials.
     */
    public static class GetCredentialsException extends Exception {
        protected String authorizationUrl;

        /**
         * Construct a GetCredentialsException.
         *
         * @param authorizationUrl The authorization URL to redirect the user to.
         */
        public GetCredentialsException(String authorizationUrl) {
            this.authorizationUrl = authorizationUrl;
        }

        /**
         * Set the authorization URL.
         */
        public void setAuthorizationUrl(String authorizationUrl) {
            this.authorizationUrl = authorizationUrl;
        }

        /**
         * @return the authorizationUrl
         */
        public String getAuthorizationUrl() {
            return authorizationUrl;
        }
    }

    /**
     * Exception thrown when a code exchange has failed.
     */
    public static class CodeExchangeException extends GetCredentialsException {
        /**
         * Construct a CodeExchangeException.
         *
         * @param authorizationUrl The authorization URL to redirect the user to.
         */
        public CodeExchangeException(String authorizationUrl) {
            super(authorizationUrl);
        }
    }

    /**
     * Exception thrown when no refresh token has been found.
     */
    public static class NoRefreshTokenException extends GetCredentialsException {
        /**
         * Construct a NoRefreshTokenException.
         *
         * @param authorizationUrl The authorization URL to redirect the user to.
         */
        public NoRefreshTokenException(String authorizationUrl) {
            super(authorizationUrl);
        }
    }

    /**
     * Exception thrown when no user ID could be retrieved.
     */
    private static class NoUserIdException extends Exception {
    }

    /**
     * Retrieved stored credentials for the provided user ID.
     *
     * @param userId User's ID.
     * @return Stored Credential if found, {@code null} otherwise.
     */
    protected Credential getStoredCredentials(String userId) {
        // Credential instance with stored accessToken and refreshToken.
        return savedCred;
    }

    /**
     * Store OAuth 2.0 credentials in the application's database.
     *
     * @param userId      User's ID.
     * @param credentials The OAuth 2.0 credentials to store.
     */
    protected void storeCredentials(String userId, Credential credentials) {
        savedCred = credentials;
    }

    /**
     * Build an authorization flow and store it as a static class attribute.
     *
     * @return GoogleAuthorizationCodeFlow instance.
     * @throws IOException Unable to load client_secret.json.
     */
    protected GoogleAuthorizationCodeFlow getFlow() throws IOException {
        if (flow == null) {
            GoogleClientSecrets clientSecret = GoogleClientSecrets.load(JSON_FACTORY, new StringReader(JSON));
            flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecret, SCOPES)
                    .setAccessType("offline")
                    .setApprovalPrompt("force")
                    .build();
        }
        return flow;
    }

    /**
     * Exchange an authorization code for OAuth 2.0 credentials.
     *
     * @param authorizationCode Authorization code to exchange for OAuth 2.0
     *                          credentials.
     * @return OAuth 2.0 credentials.
     * @throws CodeExchangeException An error occurred.
     */
    Credential exchangeCode(String authorizationCode)
            throws CodeExchangeException {
        try {
            GoogleAuthorizationCodeFlow flow = getFlow();
            GoogleTokenResponse response = flow
                    .newTokenRequest(authorizationCode)
                    .setRedirectUri(REDIRECT_URI)
                    .execute();
            return flow.createAndStoreCredential(response, null);
        } catch (IOException e) {
            System.err.println("An error occurred: " + e);
            throw new CodeExchangeException(null);
        }
    }

    /**
     * Send a request to the UserInfo API to retrieve the user's information.
     *
     * @param credentials OAuth 2.0 credentials to authorize the request.
     * @return User's information.
     * @throws NoUserIdException An error occurred.
     */
    Userinfoplus getUserInfo(Credential credentials) throws NoUserIdException {
        Oauth2 userInfoService = new Oauth2.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credentials)
                .setApplicationName(APPLICATION_NAME)
                .build();
        Userinfoplus userInfo = null;
        try {
            userInfo = userInfoService.userinfo().get().execute();
        } catch (IOException e) {
            System.err.println("An error occurred: " + e);
        }
        if (userInfo != null && userInfo.getId() != null) {
            return userInfo;
        } else {
            throw new NoUserIdException();
        }
    }

    /**
     * Retrieve the authorization URL.
     *
     * @param emailAddress User's e-mail address.
     * @param state        State for the authorization URL.
     * @return Authorization URL to redirect the user to.
     * @throws IOException Unable to load client_secret.json.
     */
    public String getAuthorizationUrl(String emailAddress, String state)
            throws IOException {
        GoogleAuthorizationCodeRequestUrl urlBuilder = getFlow()
                .newAuthorizationUrl()
                .setRedirectUri(REDIRECT_URI)
                .setState(state);
        urlBuilder.set("user_id", emailAddress);
        return urlBuilder.build();
    }

    /**
     * Retrieve credentials using the provided authorization code.
     * <p/>
     * This function exchanges the authorization code for an access token and
     * queries the UserInfo API to retrieve the user's e-mail address. If a
     * refresh token has been retrieved along with an access token, it is stored
     * in the application database using the user's e-mail address as key. If no
     * refresh token has been retrieved, the function checks in the application
     * database for one and returns it if found or throws a NoRefreshTokenException
     * with the authorization URL to redirect the user to.
     *
     * @param authorizationCode Authorization code to use to retrieve an access
     *                          token.
     * @param state             State to set to the authorization URL in case of error.
     * @return OAuth 2.0 credentials instance containing an access and refresh
     * token.
     * @throws NoRefreshTokenException No refresh token could be retrieved from
     *                                 the available sources.
     * @throws IOException             Unable to load client_secret.json.
     */
    public Credential getCredentials(String authorizationCode, String state)
            throws CodeExchangeException, NoRefreshTokenException, IOException {
        String emailAddress = "";
        try {
            Credential credentials = exchangeCode(authorizationCode);
            Userinfoplus userInfo = getUserInfo(credentials);
            String userId = userInfo.getId();
            emailAddress = userInfo.getEmail();
            if (credentials.getRefreshToken() != null) {
                storeCredentials(userId, credentials);
                return credentials;
            } else {
                credentials = getStoredCredentials(userId);
                if (credentials != null && credentials.getRefreshToken() != null) {
                    return credentials;
                }
            }
        } catch (CodeExchangeException e) {
            e.printStackTrace();
            // Drive apps should try to retrieve the user and credentials for the
            // current session.
            // If none is available, redirect the user to the authorization URL.
            e.setAuthorizationUrl(getAuthorizationUrl(emailAddress, state));
            throw e;
        } catch (NoUserIdException e) {
            e.printStackTrace();
        }
        // No refresh token has been retrieved.
        String authorizationUrl = getAuthorizationUrl(emailAddress, state);
        throw new NoRefreshTokenException(authorizationUrl);
    }

    public Drive buildService() {
        return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, savedCred)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public String createSpreadsheet(String name, String description) throws IOException {
        Drive service = buildService();
        File body = new File();
        body.setName(name);
        body.setDescription(description);
        body.setMimeType(SPREADSHEET_MIME);
        body.setParents(Arrays.asList(PARENT_FOLDER));

        File file = service.files().create(body).execute();
        LOGGER.info("Created " + file.getId() + " " + file.getName());
        return SPREADSHEET_PRE + file.getId();
    }

    public void addRow(String title, String link, String sheet) throws Exception {
        SpreadsheetService service = new SpreadsheetService(APPLICATION_NAME);
        service.setOAuth2Credentials(savedCred);

        // Define the URL to request.  This should never change.
        URL SPREADSHEET_FEED_URL = new URL("https://spreadsheets.google.com/feeds/spreadsheets/private/full");

        // Make a request to the API and get all spreadsheets.
        SpreadsheetFeed feed = service.getFeed(SPREADSHEET_FEED_URL, SpreadsheetFeed.class);
        List<SpreadsheetEntry> spreadsheets = feed.getEntries();

        if (spreadsheets.size() == 0) {
            LOGGER.error("No spreadsheets found, so that won't work");
            return;
        }

        SpreadsheetEntry spreadsheet = null;
        for (SpreadsheetEntry entry : spreadsheets) {
            //LOGGER.info("Spreadsheet Entry: " + entry.getId() + " key: " + entry.getKey());
            if (entry.getKey().equals(MASTER_SHEET_ID)) {
                spreadsheet = entry;
                break;
            }
        }

        if (spreadsheet == null) {
            LOGGER.error("Could not find master spreadsheet, boo and/or hiss");
            return;
        }

        WorksheetFeed worksheetFeed = service.getFeed(
                spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
        List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
        WorksheetEntry worksheet = worksheets.get(0);

        // Fetch the list feed of the worksheet.
        URL listFeedUrl = worksheet.getListFeedUrl();
        ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);

        // Create a local representation of the new row.
        ListEntry row = new ListEntry();
        row.getCustomElements().setValueLocal("puzzleName", title);
        row.getCustomElements().setValueLocal("puzzleLink", link);
        row.getCustomElements().setValueLocal("puzzleSheet", sheet);
        row.getCustomElements().setValueLocal("status", "Open");

        // Send the new row to the API for insertion.
        row = service.insert(listFeedUrl, row);
    }
}