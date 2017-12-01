/**
 * Language Detector - Natural language detection application <br>
 * The class illustrates how to write comments used 
 * to generate JavaDoc documentation
 *
 * @url https://git.muratti66.com:8443/mbudak/languagedetector
 */
package com.muratti66.languagedetector;

import com.datumbox.framework.applications.nlp.TextClassifier;
import com.datumbox.framework.common.Configuration;
import com.datumbox.framework.core.common.dataobjects.Record;
import com.datumbox.framework.common.utilities.RandomGenerator;
import com.datumbox.framework.core.machinelearning.MLBuilder;
import com.datumbox.framework.core.machinelearning.classification.MultinomialNaiveBayes;
import com.datumbox.framework.core.machinelearning.featureselection.ChisquareSelect;
import com.datumbox.framework.core.common.text.extractors.NgramsExtractor;
import com.datumbox.framework.core.machinelearning.preprocessing.CornerConstraintsEncoder;
import java.io.File;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import static com.muratti66.languagedetector.Main.langFilesPath;
import static com.muratti66.languagedetector.Main.maxNumberOfThreadsPerTask;
import static com.muratti66.languagedetector.Main.setParallelized;
import static com.muratti66.languagedetector.Main.setGlobalSeed;

/** - Language Detector Learn Operation Class <p>
 * 
 * @author Murat B.
 * @version 1.00, 01 Dec 2017
 * @since 1.0
 */
public class LearnOperation {
    
    private static TextClassifier textClassifier = null;
    private final static Logger LOGGER = Logger.getLogger(LearnOperation.class);
    /**
     * Training for Learn Operation
     */
    public static void initialization() {
        Map<Object, URI> datasets = new HashMap<>();
        
        RandomGenerator.setGlobalSeed(setGlobalSeed);
        Configuration configuration = Configuration.getConfiguration();
        configuration.getConcurrencyConfiguration().setParallelized(setParallelized);
        configuration.getConcurrencyConfiguration()
                .setMaxNumberOfThreadsPerTask(maxNumberOfThreadsPerTask);
        
        File curDir = new File(langFilesPath);
        File[] filesList = curDir.listFiles();
        for(File f : filesList){
            datasets.put(f.getName(), f.toURI());
        }
        TextClassifier.TrainingParameters trainingParameters = 
                new TextClassifier.TrainingParameters();
        trainingParameters.setNumericalScalerTrainingParameters(null);
        trainingParameters.setCategoricalEncoderTrainingParameters(
                new CornerConstraintsEncoder.TrainingParameters());
        trainingParameters.setFeatureSelectorTrainingParametersList(
                Arrays.asList(new ChisquareSelect.TrainingParameters()));
        trainingParameters.setTextExtractorParameters(
                new NgramsExtractor.Parameters());
        trainingParameters.setModelerTrainingParameters(
                new MultinomialNaiveBayes.TrainingParameters());
        
        textClassifier = MLBuilder.create(trainingParameters, configuration);
        textClassifier.fit(datasets);
        textClassifier.save("LanguageAnalysis");
    }
    /**
     * Checking sentence in Trained Data
     * @param sentence Checking string
     * @return Sepeated string result
     */
    public static String checkData(String sentence) {
        if (textClassifier == null) {
            LOGGER.warn("Text classifier is null!! , passing query ...");
            return null;
        }
        Record r = textClassifier.predict(sentence);
        return r.getYPredicted().toString() + ";;" 
                + r.getYPredictedProbabilities().get(r.getYPredicted()).toString();
    }
}
