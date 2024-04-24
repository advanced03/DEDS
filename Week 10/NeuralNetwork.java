import java.util.Random;
//Neurale netwerk klasse
public class NeuralNetwork {
    private int inputNodes = 4;
    private int hiddenNodes;
    private double[][] inputToHiddenWeights;
    private double[] hiddenToOutputWeights;
    private double[] hiddenOutputs;

    public NeuralNetwork(int hiddenNodes) {
        this.hiddenNodes = hiddenNodes;
        initializeWeights();
    }
//Weights methode
    private void initializeWeights() {
        Random random = new Random();
        inputToHiddenWeights = new double[inputNodes][hiddenNodes];
        hiddenToOutputWeights = new double[hiddenNodes];
        for (int i = 0; i < inputNodes; i++) {
            for (int j = 0; j < hiddenNodes; j++) {
                inputToHiddenWeights[i][j] = random.nextDouble() - 0.5;
            }
        }
        for (int i = 0; i < hiddenNodes; i++) {
            hiddenToOutputWeights[i] = random.nextDouble() - 0.5; 
        }
    }
//Feedforward methode
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

    //Mogelijke oplossingen een fitness score geven en dan steeds muteren
    public void trainGeneticAlgorithm(double[][] inputs, double[] targets, int populationSize, int generations) {
        NeuralNetwork[] population = new NeuralNetwork[populationSize];
        for (int i = 0; i < populationSize; i++) {
            population[i] = new NeuralNetwork(hiddenNodes);
        }

        for (int generation = 0; generation < generations; generation++) {
            double[] fitnessScores = new double[populationSize];
            for (int i = 0; i < populationSize; i++) {
                double totalError = 0;
                for (int j = 0; j < inputs.length; j++) {
                    double[] prediction = population[i].feedForward(inputs[j]);
                    totalError += Math.abs(targets[j] - prediction[0]);
                }
                fitnessScores[i] = totalError; 
            }

            NeuralNetwork[] newPopulation = new NeuralNetwork[populationSize];
            for (int i = 0; i < populationSize; i++) {
                NeuralNetwork parent1 = selectParent(population, fitnessScores);
                NeuralNetwork parent2 = selectParent(population, fitnessScores);
                NeuralNetwork child = crossover(parent1, parent2);
                mutate(child);
                newPopulation[i] = child;
            }

            population = newPopulation;
        }
        //Beste fitness selecteren van alle gemuteerde oplosssingen
        double bestFitness = Double.MAX_VALUE;
        NeuralNetwork bestNetwork = null;
        for (NeuralNetwork network : population) {
            double totalError = 0;
            for (int j = 0; j < inputs.length; j++) {
                double[] prediction = network.feedForward(inputs[j]);
                totalError += Math.abs(targets[j] - prediction[0]);
            }
            if (totalError < bestFitness) {
                bestFitness = totalError;
                bestNetwork = network;
            }
        }

        System.out.println("Best Network Predictions:");
        for (int i = 0; i < inputs.length; i++) {
            double[] prediction = bestNetwork.feedForward(inputs[i]);
            System.out.println("Input: " + java.util.Arrays.toString(inputs[i]) + " Prediction: " + prediction[0]);
        }
    }

    private NeuralNetwork selectParent(NeuralNetwork[] population, double[] fitnessScores) {
        Random random = new Random();
        int index = random.nextInt(population.length);
        return population[index];
    }

    private NeuralNetwork crossover(NeuralNetwork parent1, NeuralNetwork parent2) {
        Random random = new Random();
        NeuralNetwork child = new NeuralNetwork(parent1.hiddenNodes);
        for (int i = 0; i < child.inputToHiddenWeights.length; i++) {
            for (int j = 0; j < child.inputToHiddenWeights[i].length; j++) {
                if (random.nextBoolean()) {
                    child.inputToHiddenWeights[i][j] = parent1.inputToHiddenWeights[i][j];
                } else {
                    child.inputToHiddenWeights[i][j] = parent2.inputToHiddenWeights[i][j];
                }
            }
        }
        for (int i = 0; i < child.hiddenToOutputWeights.length; i++) {
            if (random.nextBoolean()) {
                child.hiddenToOutputWeights[i] = parent1.hiddenToOutputWeights[i];
            } else {
                child.hiddenToOutputWeights[i] = parent2.hiddenToOutputWeights[i];
            }
        }
        return child;
    }

    //Methode om oplossing te muteren
    private void mutate(NeuralNetwork network) {
        Random random = new Random();
        for (int i = 0; i < network.inputToHiddenWeights.length; i++) {
            for (int j = 0; j < network.inputToHiddenWeights[i].length; j++) {
                if (random.nextDouble() < mutationRate) {
                    network.inputToHiddenWeights[i][j] += random.nextGaussian() * mutationRange;
                }
            }
        }
        for (int i = 0; i < network.hiddenToOutputWeights.length; i++) {
            if (random.nextDouble() < mutationRate) {
                network.hiddenToOutputWeights[i] += random.nextGaussian() * mutationRange;
            }
        }
    }

    private double mutationRate = 0.1;
    private double mutationRange = 0.1;

    public static void main(String[] args) {
        int hiddenNodes = 3;

        NeuralNetwork neuralNetwork = new NeuralNetwork(hiddenNodes);

        double[][] inputs = {
            {0, 0, 0, 0},
            {0, 0, 1, 1},
            {0, 1, 0, 1},
            {1, 0, 0, 1},
            {1, 1, 1, 0}
        };
        double[] targets = {0, 1, 1, 1, 0};

        int populationSize = 50; 
        int generations = 100; 
        neuralNetwork.trainGeneticAlgorithm(inputs, targets, populationSize, generations);
    }
}

