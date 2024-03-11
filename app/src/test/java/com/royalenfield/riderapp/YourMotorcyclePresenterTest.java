package com.royalenfield.riderapp;

import com.royalenfield.reprime.models.response.web.vehicledetails.VehicleData;
import com.royalenfield.reprime.ui.userdatavalidation.popup.presenter.PopUpPresenter;
import com.royalenfield.reprime.ui.userdatavalidation.popup.views.PopUpView;
import com.royalenfield.reprime.ui.userdatavalidation.singlemotorcycle.interactor.YourMotorcycleInteractor;
import com.royalenfield.reprime.ui.userdatavalidation.singlemotorcycle.presenter.YourMotorcyclePresenter;
import com.royalenfield.reprime.ui.userdatavalidation.singlemotorcycle.view.UpdateVehicleData;
import com.royalenfield.reprime.ui.userdatavalidation.singlemotorcycle.views.YourMotorcycleView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
public class YourMotorcyclePresenterTest {

    @Mock
    private YourMotorcycleView mYourMotorcycleView;
    @Mock
    UpdateVehicleData vehicleData;

    private YourMotorcyclePresenter mYourMotorcyclePresenter;

    @Before
    public void setupYourMotorcyclePresenter() throws Exception {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);
        mYourMotorcyclePresenter = new YourMotorcyclePresenter(mYourMotorcycleView, new YourMotorcycleInteractor());
    }

    @Test
    public void shouldShowErrorMessageWhenRegistrationNumIsEmpty() throws Exception {
        mYourMotorcyclePresenter.onConfirmButtonClicked(vehicleData, "");
        verify(mYourMotorcycleView).showEmptyError();
    }

    @Test
    public void shouldShowErrorMessageWhenRegistrationNumIsBelowRange() throws Exception {
        mYourMotorcyclePresenter.onConfirmButtonClicked(vehicleData, "111");
        verify(mYourMotorcycleView).showNumRangeError();
    }

    @Test
    public void shouldShowErrorMessageWhenRegistrationNumIsAboveRange() throws Exception {
        mYourMotorcyclePresenter.onConfirmButtonClicked(vehicleData, "111555555555");
        verify(mYourMotorcycleView).showNumRangeError();
    }
}
