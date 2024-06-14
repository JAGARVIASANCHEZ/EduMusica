package com.example.edumusicav2;


/**
 * Clase que representa una pregunta de intervalo musical.
 */
public class PreguntaIntervalo {
    private String nombreAudio;
    private String[] opciones;
    private String opcionCorrecta;


    /**
     * Constructor para crear una nueva pregunta de intervalo.
     *
     * @param audio     Nombre del archivo de audio del intervalo.
     * @param arrayopciones       Opciones de respuesta para la pregunta.
     * @param opcionCorrecta Respuesta correcta para la pregunta.
     */
    public PreguntaIntervalo(String audio, String[] arrayopciones, String opcionCorrecta) {
        this.nombreAudio = audio;
        this.opciones = arrayopciones;
        this.opcionCorrecta = opcionCorrecta;
    }



    public String getNombreAudio() {
        return nombreAudio;
    }

    public String[] getOpciones() {
        return opciones;
    }

    public String getOpcionCorrecta() {
        return opcionCorrecta;
    }
}
