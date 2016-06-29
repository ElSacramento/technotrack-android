package com.example.clay.myapplication;

/**
 * Created by clay on 17.05.16.
 */
public class Errors {
    public String getError(int status){
        String s;
        switch (status){
            case 0: s = "Все хорошо";
                    break;
            case 1: s = "Логин или ник или канал уже заняты";
                    break;
            case 2: s = "Невалидный логин или пароль";
                    break;
            case 3: s = "Невалидный JSON";
                    break;
            case 4: s = "Пустой ник, логин, пароль или имя канала";
                    break;
            case 5: s = "Пользователь уже зарегистрирован";
                    break;
            case 6: s = "Нужна авторизация";
                    break;
            case 7: s = "Нужна регистрация";
                    break;
            case 8: s = "По указанному UserId не найден пользователь";
                    break;
            case 9: s = "Не найден канал по ID";
                    break;
            case 10:s = "Пользователь не в канале";
                    break;
            default:s = "";
                    break;
        }
        return s;
    }
}
