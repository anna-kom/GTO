package com.russiantest.gto;

/* класс вопроса. нужен чтобы различать типы вопросов
 * типов 3:
 * Первый тип (Standard) - текст вопроса и 4 варианта ответа (например вопросы 1, 2)
 * Второй тип (Commas) - где нужно выбирать запятые: текст вопросы, дополнительный текст и 4 варианта ответа (например 13)
 * Третий тип (Input) - где нудно писать свой ответ: текст вопроса, дополнительный текст и поле для ввода (например 17, 18)
 */

import java.util.List;

public class Question {

    private QuestionType type; // тип вопроса
    private String text; // сам текст вопроса
    private String additionalText; //  доп. текст (для второго и третьего типов)
    private List<String> options; // варианты ответа (для первого и второго типов)
    private String answer;
    private int points;

    public QuestionType getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public String getAdditionalText() {
        return additionalText;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getPoints() {
        return points;
    }

    public String getAnswer() { return answer; }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setAdditionalText(String additionalText) {
        this.additionalText = additionalText;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
