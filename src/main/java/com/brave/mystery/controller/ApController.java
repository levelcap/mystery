package com.brave.mystery.controller;

import com.brave.mystery.model.OvercomeResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class ApController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApController.class);

    @RequestMapping("/api/fate")
    public Map<String, Object> testObstacle(@RequestParam(value = "difficulty") Integer difficulty,
                                            @RequestParam(value = "bonus") Integer bonus,
                                            @RequestParam(value = "advantageDifficulty") Integer advantageDifficulty,
                                            @RequestParam(value = "helpers") Integer helpers,
                                            @RequestParam(value = "run") Integer run) {

        Map<String, Object> analysis = new HashMap<String, Object>();
        List<OvercomeResult> results = new ArrayList<OvercomeResult>();

        Integer totalFailures = 0;
        Integer totalTies = 0;
        Integer totalSuccesses = 0;
        Integer totalSuccessesWithStyle = 0;
        Double totalAdvantages = 0.0;

        for (int i = 0; i < run; i++) {
            OvercomeResult result = new OvercomeResult(difficulty, randomFateRoll(bonus));
            boolean boost = false;
            for (int j = 0; j < helpers; j++) {
                int helpRoll = randomFateRoll(bonus);
                if (!boost && helpRoll >= advantageDifficulty) {
                    result.addSuccessfulHelper();
                    totalAdvantages++;
                    if (helpRoll == advantageDifficulty) {
                        boost = true;
                        result.setBooster(j + 1);
                    }
                } else {
                    result.addFailedHelper();
                }
            }
            result.calculateOutcome();
            switch (result.getOutcome()) {
                case FAIL:
                    totalFailures++;
                    break;
                case TIE:
                    totalTies++;
                    break;
                case SUCCESS:
                    totalSuccesses++;
                    break;
                case SUCCESS_WITH_STYLE:
                    totalSuccessesWithStyle++;
                    break;
            }
            results.add(result);
        }

        Double averageAdvantages = totalAdvantages / run;

        analysis.put("fails", totalFailures);
        analysis.put("ties", totalTies);
        analysis.put("successes", totalSuccesses);
        analysis.put("withStyles", totalSuccessesWithStyle);
        analysis.put("avgAdvantage", averageAdvantages);
        analysis.put("results", results);
        return analysis;
    }

    private int randomFateRoll(int bonus) {
        Random rand = new Random();
        return (-4 + rand.nextInt((4 - -4) + 1)) + bonus;
    }
}
