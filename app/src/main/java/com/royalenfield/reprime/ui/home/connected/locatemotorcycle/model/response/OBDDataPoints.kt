package com.royalenfield.reprime.ui.home.connected.locatemotorcycle.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

 class OBDDataPoints {

    @SerializedName("ts")
    @Expose
    var ts: String = ""

    @SerializedName("lat")
    @Expose
    var lat: Double? = null

    @SerializedName("lng")
    @Expose
    var lng: Double? = null

    @SerializedName("fuel_pump_status")
    @Expose
    var fuelPumpStatus: String = ""

    @SerializedName("injection_mode")
    @Expose
    var injectionMode: String = ""

    @SerializedName("ignition_mode")
    @Expose
    var ignitionMode: String = ""

    @SerializedName("mil_state")
    @Expose
    var milState: String = ""

    @SerializedName("rpm")
    @Expose
    var rpm: Double? = null

    @SerializedName("engine_state")
    @Expose
    var engineState: String = ""

    @SerializedName("idle_speed")
    @Expose
    var idleSpeed: String = ""

    @SerializedName("timer_inclination")
    @Expose
    var timerInclination: String = ""

    @SerializedName("cp_mode")
    @Expose
    var cpMode: String = ""

    @SerializedName("isc_activation")
    @Expose
    var iscActivation: String = ""

    @SerializedName("input_battery_voltage")
    @Expose
    var inputBatteryVoltage: String = ""

    @SerializedName("tilt_switch")
    @Expose
    var tiltSwitch: String = ""

    @SerializedName("engine_stop_request")
    @Expose
    var engineStopRequest: String = ""

    @SerializedName("VB_Snapshot")
    @Expose
    var VBSnapshot: String = ""

    @SerializedName("intakeAirTemp")
    @Expose
    var intakeAirTemp: String = ""

    @SerializedName("maf_throttle")
    @Expose
    var mafThrottle: String = ""

    @SerializedName("maf")
    @Expose
    var maf: String = ""

    @SerializedName("map_throttle")
    @Expose
    var mapThrottle: String = ""

    @SerializedName("imap")
    @Expose
    var imap: String = ""

    @SerializedName("V_TPS_BAS")
    @Expose
    var vTpsBAS: String = ""

    @SerializedName("TPS_DEG")
    @Expose
    var TpsDeg: String = ""

    @SerializedName("raw_angle")
    @Expose
    var rawAngle: String = ""

    @SerializedName("oxy_sensor_voltage")
    @Expose
    var oxySensorVoltage: String = ""

    @SerializedName("lambda_setpoint")
    @Expose
    var lambdaSetPoint: String = ""

    @SerializedName("inj_timing")
    @Expose
    var injTiming: String = ""

    @SerializedName("coolant")
    @Expose
    var coolant: String = ""

    @SerializedName("vehicleSpeed")
    @Expose
    var vehicleSpeed: String = ""

    @SerializedName("VB_MMV")
    @Expose
    var VbMmv: String = ""

    @SerializedName("mffi_homogeneous_mode")
    @Expose
    var mffiHomogeneousMode: String = ""

    @SerializedName("atmospheric_pressure")
    @Expose
    var atmosphericPressure: String = ""

    @SerializedName("V_TPS_AD_BOL")
    @Expose
    var VTpsAdBol: String = ""

    @SerializedName("ISAPWM_AD_MMV")
    @Expose
    var IsapwmAdMmv: String = ""

    @SerializedName("TI_HOM_INJ")
    @Expose
    var TiHomInj: String = ""

    @SerializedName("IGA_CYL")
    @Expose
    var IgaCyl: String = ""

    @SerializedName("MFF_ADAP_ADD")
    @Expose
    var MffAdapAdd: String = ""

    @SerializedName("MFF_ADAP_FAC")
    @Expose
    var MffAdapFac: String = ""

    @SerializedName("idle_charge")
    @Expose
    var idleCharge: String = ""

    @SerializedName("IGA_ISC")
    @Expose
    var IgaIsc: String = ""

    @SerializedName("TI_MES_INJ")
    @Expose
    var TiMesInj: String = ""

    @SerializedName("clutch_state")
    @Expose
    var clutchState: String = ""

    @SerializedName("duty_cycle")
    @Expose
    var dutyCycle: String = ""

    @SerializedName("ERT")
    @Expose
    var ert: String = ""

    @SerializedName("tilt_switch_number")
    @Expose
    var tiltSwitchNumber: String = ""

    @SerializedName("TILT_AD_DGNC")
    @Expose
    var tiltAdDgnc: String = ""

    @SerializedName("obdDistance")
    @Expose
    var obdDistance: String = "0.0"

    @SerializedName("bank_angle")
    @Expose
    var bankAngle: String = ""

    @SerializedName("tankLevel")
    @Expose
    var tankLevel: String = ""


    @SerializedName("pluscode")
    @Expose
    var plusCode: String = ""

    @SerializedName("low_fuel")
    @Expose
    var lowFuel: Int = 0

    /*
    *  Extra keys not in latest mail
    * */

    @SerializedName("internal_bat_voltage")
    @Expose
    var internalBatVoltage: Int = 0

    @SerializedName("engineload")
    @Expose
    var engineload: Int = 0

    @SerializedName("throttle")
    @Expose
    var throttle: Int = 0

    @SerializedName("engineoiltemp")
    @Expose
    var engineoiltemp: Int = 0

    @SerializedName("warmups")
    @Expose
    var warmups: Int = 0

    @SerializedName("fuelrail")
    @Expose
    var fuelrail: Int = 0

    @SerializedName("runtime")
    @Expose
    var runtime: Int = 0

    @SerializedName("mil")
    @Expose
    var mil: Int = 0

    @SerializedName("obdstandards")
    @Expose
    var obdstandards: String = ""

    @SerializedName("telltale")
    @Expose
    var telltale: String = ""

    @SerializedName("selected_gear")
    @Expose
    var selectedGear: String = ""

    @SerializedName("current_gear")
    @Expose
    var currentGear: String = ""

    @SerializedName("fuel_level")
    @Expose
    var fuelLevel: Int = 0

    @SerializedName("power")
    @Expose
    var power: Int = 0

    @SerializedName("battery_pack_current")
    @Expose
    var batteryPackCurrent: Int = 0

    @SerializedName("battery_pack_voltage")
    @Expose
    var batteryPackVoltage: Int = 0

    @SerializedName("vehiclenumber")
    @Expose
    var vehiclenumber: String=""

    @SerializedName("gps_status")
    @Expose
    var gpsStatus: Int=-1

    @SerializedName("gsm_status")
    @Expose
    var gsmStatus: Int=-1

    @SerializedName("tripfstate")
    @Expose
    var tripfstate: Int=0


}
