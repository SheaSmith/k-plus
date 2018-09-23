package sheasmith.me.betterkamar.internalModels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sheasmith.me.betterkamar.dataModels.ResultObject;

/**
 * Created by TheDiamondPicks on 22/09/2018.
 */
public class ResultsViewModel {
    public boolean isLevel = false;
    public String level;

    public String title;
    public List<ResultObject.Result> results;

    public boolean expanded = false;
    public boolean justRotated = false;
    public int angle = 0;

    public ResultsViewModel(String title, List<ResultObject.Result> results) {
        isLevel = false;
        this.title = title;
        this.results = results;
    }

    public ResultsViewModel(ResultObject.ResultLevel level) {
        isLevel = true;
        this.level = level.NCEALevel;
    }

    public static List<ResultsViewModel> generate(ResultObject.ResultLevel level) {
        List<ResultsViewModel> results = new ArrayList<>();
        List<String> tempList = new ArrayList<>();

        results.add(new ResultsViewModel(level));
        for (ResultObject.Result result : level.Results) {
            if (!tempList.contains(result.SubField)) {
                tempList.add(result.SubField);
                results.add(new ResultsViewModel(result.SubField, Arrays.asList(result)));
            }
            else {
                results.get(tempList.indexOf(result.SubField) + 1).results.add(result);
            }
        }

        return results;
    }

    public static List<ResultsViewModel> generate(ResultObject resultObject) {
        List<ResultsViewModel> results = new ArrayList<>();
        for (ResultObject.ResultLevel level : resultObject.StudentResultsResults.ResultLevels) {
            List<String> tempList = new ArrayList<>();

            results.add(new ResultsViewModel(level));
            for (ResultObject.Result result : level.Results) {
                if (!tempList.contains(result.SubField)) {
                    tempList.add(result.SubField);
                    List<ResultObject.Result> resultsList = new ArrayList<>();
                    resultsList.add(result);
                    results.add(new ResultsViewModel(result.SubField, resultsList));
                } else {
                    int i;
                    for (i = results.size() - 1 ; i != -1 ; i--) {
                        if (results.get(i).title.equals(result.SubField))
                            break;
                    }
                    results.get(i).results.add(result);
                }
            }
        }

        return results;
    }
}
