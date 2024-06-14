package com.example.edumusicav2;

public class PreguntaIntervalo {
    private String audioName;
    private String[] choices;
    private String correctAnswer;

    public PreguntaIntervalo(String audioName, String[] choices, String correctAnswer) {
        this.audioName = audioName;
        this.choices = choices;
        this.correctAnswer = correctAnswer;
    }

    public String getAudioName() {
        return audioName;
    }

    public String[] getChoices() {
        return choices;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
