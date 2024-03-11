package com.royalenfield.reprime.ui.userdatavalidation.singlemotorcycle.views;

public interface YourMotorcycleView {
    void showConfirmationScreen(int id);

    void showEmptyError();

    void showNumRangeError();

    void showAlphaNumericError();

    void showNumberError();

    void showLettersError();

    void hideRegistartionError();
}
