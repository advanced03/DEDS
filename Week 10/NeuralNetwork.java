import java.util.Random;

public class NeuralNetwork {
    private int inputNodes = 4;
    private int hiddenNodes;
    private int outputNodes = 1;
    private double[][] inputToHiddenWeights;
    private double[] hiddenToOutputWeights;
    private double[] hiddenOutputs;

    public NeuralNetwork(int hiddenNodes) {
        this.hiddenNodes = hiddenNodes;
        initializeWeights();
    }

    private void initializeWeights() {
        Random random = new Random();
        inputToHiddenWeights = new double[inputNodes][hiddenNodes];
        hiddenToOutputWeights = new double[hiddenNodes];
        for (int i = 0; i < inputNodes; i++) {
            for (int j = 0; j < hiddenNodes; j++) {
                inputToHiddenWeights[i][j] = random.nextDouble() - 0.5; // Random weights between -0.5 and 0.5
            }
        }
        for (int i = 0; i < hiddenNodes; i++) {
            hiddenToOutputWeights[i] = random.nextDouble() - 0.5; // Random weights between -0.5 and 0.5
        }
    }

    public double[] feedForward(double[] input) {
        hiddenOutputs = new double[hiddenNodes];
        for (int i = 0; i < hiddenNodes; i++) {
            double weightedSum = 0;
            for (int j = 0; j < inputNodes; j++) {
                weightedSum += input[j] * inputToHiddenWeights[j][i];
            }
            hiddenOutputs[i] = sigmoid(weightedSum);
        }
        double output = 0;
        for (int i = 0; i < hiddenNodes; i++) {
            output += hiddenOutputs[i] * hiddenToOutputWeights[i];
        }
        return new double[]{sigmoid(output)};
    }

    private double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    public void train(double[][] inputs, double[] targets, int epochs) {
        for (int epoch = 0; epoch < epochs; epoch++) {
            for (int i = 0; i < inputs.length; i++) {
                double[] prediction = feedForward(inputs[i]);
                double error = targets[i] - prediction[0];
                for (int j = 0; j < hiddenNodes; j++) {
                    for (int k = 0; k < inputNodes; k++) {
                        inputToHiddenWeights[k][j] += error * inputs[i][k];
                    }
                    hiddenToOutputWeights[j] += error;
                }
            }
        }
    }
    

    public static void main(String[] args) {
        // Definieer de parameters van het neurale netwerk
        int hiddenNodes = 3;
        int epochs = 1000;

        // Initialisatie van het neurale netwerk
        NeuralNetwork neuralNetwork = new NeuralNetwork(hiddenNodes);

        // Definieer de input datapunten en bijbehorende output
        double[][] inputs = {
            {0, 0, 0, 0},
            {0, 0, 1, 1},
            {0, 1, 0, 1},
            {1, 0, 0, 1},
            {1, 1, 1, 0}
        };
        double[] targets = {0, 1, 1, 1, 0};

        // Train het neurale netwerk
        neuralNetwork.train(inputs, targets, epochs);

        // Test het getrainde neurale netwerk
        System.out.println("Predictions:");
        for (int i = 0; i < inputs.length; i++) {
            double[] prediction = neuralNetwork.feedForward(inputs[i]);
            System.out.println("Input: " + java.util.Arrays.toString(inputs[i]) + " Prediction: " + prediction[0]);
        }
    }
}
