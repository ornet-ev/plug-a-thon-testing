package org.ornet.pat.mdib.v2

import org.ornet.pat.localization.db.v1.LocalizationServiceHandler
import org.ornet.pat.mdib.v2.resource.ACTIVATE_OPERATION_1
import org.ornet.pat.mdib.v2.resource.ACTIVATE_OPERATION_ARGUMENT_1
import org.ornet.pat.mdib.v2.resource.ACTIVATE_OPERATION_ARGUMENT_2
import org.ornet.pat.mdib.v2.resource.ACTIVATE_OPERATION_ARGUMENT_3
import org.ornet.pat.mdib.v2.resource.ENUM_VALUE_OFF
import org.ornet.pat.mdib.v2.resource.ENUM_VALUE_ON
import org.ornet.pat.mdib.v2.resource.ENUM_VALUE_STANDBY
import org.ornet.pat.mdib.v2.resource.EN_US
import org.ornet.pat.mdib.v2.resource.Handles
import org.ornet.pat.mdib.v2.resource.METRIC_TARGETED_BY_ACTIVATE_OPERATION_2
import org.ornet.pat.mdib.v2.resource.SAMPLE_ALERT_CONDITION_1
import org.ornet.pat.mdib.v2.resource.SAMPLE_ALERT_CONDITION_2
import org.ornet.pat.mdib.v2.resource.SAMPLE_CHANNEL_1
import org.ornet.pat.mdib.v2.resource.SAMPLE_CHANNEL_2
import org.ornet.pat.mdib.v2.resource.SAMPLE_CHANNEL_3
import org.ornet.pat.mdib.v2.resource.SAMPLE_MDS_1
import org.ornet.pat.mdib.v2.resource.SAMPLE_MDS_2
import org.ornet.pat.mdib.v2.resource.SAMPLE_METRIC_1
import org.ornet.pat.mdib.v2.resource.SAMPLE_METRIC_2
import org.ornet.pat.mdib.v2.resource.SAMPLE_METRIC_3
import org.ornet.pat.mdib.v2.resource.SAMPLE_METRIC_4
import org.ornet.pat.mdib.v2.resource.SAMPLE_METRIC_5
import org.ornet.pat.mdib.v2.resource.SAMPLE_METRIC_6
import org.ornet.pat.mdib.v2.resource.SAMPLE_VMD_1
import org.ornet.pat.mdib.v2.resource.SAMPLE_VMD_2
import org.ornet.pat.mdib.v2.resource.SET_ENUM_STRING_METRIC_VALUE
import org.ornet.pat.mdib.v2.resource.SET_MULTIPLE_NUMERIC_METRIC_VALUE
import org.ornet.pat.mdib.v2.resource.SET_NUMERIC_METRIC_VALUE
import org.ornet.pat.mdib.v2.resource.SET_PATIENT_CONTEXT
import org.ornet.pat.mdib.v2.resource.WAVEFORM_METRIC_1
import org.ornet.pat.mdib.v2.resource.WAVEFORM_METRIC_2
import org.ornet.pat.mdib.v2.resource.WAVEFORM_METRIC_3
import org.ornet.pat.mdib.v2.resource.WAVEFORM_METRIC_4
import org.ornet.pat.mdib.v2.resource.XSD_ANY_URI
import org.ornet.pat.mdib.v2.resource.XSD_DECIMAL
import org.ornet.pat.mdib.v2.resource.XSD_STRING
import org.somda.dsl.biceps.Mdib
import org.somda.dsl.biceps.base.AlertConditionKind
import org.somda.dsl.biceps.base.AlertConditionPriority
import org.somda.dsl.biceps.base.AlertSignalManifestation
import org.somda.dsl.biceps.base.MetricAvailability
import org.somda.dsl.biceps.base.MetricCategory
import org.somda.dsl.biceps.base.SafetyClassification
import org.somda.dsl.biceps.base.codedValue
import org.somda.dsl.biceps.base.decimal
import org.somda.dsl.biceps.base.range
import org.somda.dsl.biceps.mdib
import org.somda.dsl.ieee.nomenclature.CFC_532224
import org.somda.dsl.ieee.nomenclature.CFC_532225
import org.somda.dsl.ieee.nomenclature.CFC_532234
import org.somda.dsl.ieee.nomenclature.MDC_ATTR_ID_SOFT
import org.somda.dsl.ieee.nomenclature.MDC_DIM_DIMLESS
import org.somda.dsl.ieee.nomenclature.MDC_DIM_MILLI_L_PER_HR
import org.somda.dsl.ieee.nomenclature.MDC_DIM_X_AMP_HR
import org.somda.dsl.ieee.nomenclature.MDC_DIM_X_VOLT
import org.somda.dsl.ieee.nomenclature.MDC_FLOW_FLUID_PUMP
import org.somda.dsl.sdpi.codedAttributes
import java.io.File
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds


fun patMdibV2(localizedTextOutputDir: File): Mdib {
    return mdib("PlugathonMdibV2") {
        installPostProcessor(LocalizationServiceHandler(localizedTextOutputDir))

        mds(Handles.MDS_0) {
            extendWith {
                codedAttributes {
                    codedString(
                        type = MDC_ATTR_ID_SOFT,
                        value = "equipment-label"
                    )
                }
            }

            type(SAMPLE_MDS_1) {
                conceptDescription(EN_US) { "SDPi Test MDS" }
            }

            alertSystem(Handles.ALERT_SYSTEM_MDS_0) {
                selfCheckPeriod(5.seconds)

                limitAlertCondition(
                    handle = Handles.ALERT_CONDITION_0_MDS_0,
                    kind = AlertConditionKind.PHYSIOLOGICAL,
                    priority = AlertConditionPriority.MEDIUM,
                    maxLimits = range {
                        lower(decimal(10))
                        upper(decimal(90))
                    }
                ) {
                    type(SAMPLE_ALERT_CONDITION_1)
                    source(Handles.NUMERIC_METRIC_1_CHANNEL_1_VMD_0_MDS_0.ref)

                    alertSignal(
                        handle = Handles.ALERT_SIGNAL_0_MDS_0,
                        manifestation = AlertSignalManifestation.VISIBLE
                    ) {
                        latching()
                    }
                }
            }

            sco(Handles.SCO_MDS_0) {
                setContextStateOperation(
                    handle = Handles.SET_CONTEXT_0_SCO_MDS_0,
                    operationTarget = Handles.PATIENT_CONTEXT_MDS_0.ref
                ) {
                    type(SET_PATIENT_CONTEXT) {
                        conceptDescription(EN_US) {
                            "Adds a exactly one Patient Context to the operation target by using the attributes and " +
                                    "elements from the SCO Operation's payload"
                        }
                    }
                    maxTimeToFinish(1.seconds)
                    safetyClassification(SafetyClassification.MED_A)
                }

                setValueOperation(
                    handle = Handles.SET_VALUE_0_SCO_MDS_0,
                    operationTarget = Handles.NUMERIC_METRIC_0_CHANNEL_0_VMD_0_MDS_0.ref
                ) {
                    type(SET_NUMERIC_METRIC_VALUE) {
                        conceptDescription(EN_US) { "Sets the value of the targeted Numeric Metric" }
                    }

                    safetyClassification(SafetyClassification.MED_A)
                }

                setStringOperation(
                    handle = Handles.SET_STRING_0_SCO_MDS_0,
                    operationTarget = Handles.ENUM_STRING_METRIC_0_CHANNEL_0_VMD_0_MDS_0.ref
                ) {
                    type(SET_ENUM_STRING_METRIC_VALUE) {
                        conceptDescription(EN_US) { "Sets the value of the targeted Enum String Metric" }
                    }

                    safetyClassification(SafetyClassification.MED_A)
                }

                activateOperation(
                    handle = Handles.ACTIVATE_0_SCO_MDS_0,
                    operationTarget = Handles.MDS_0.ref
                ) {
                    type(ACTIVATE_OPERATION_1) {
                        conceptDescription(EN_US) {
                            "Performs nothing. The operational state will be toggled periodically at least every " +
                                    "5 seconds in order to produce Operational State Reports."
                        }
                    }
                }

                activateOperation(
                    handle = Handles.ACTIVATE_1_SCO_MDS_0,
                    operationTarget = Handles.STRING_METRIC_1_CHANNEL_0_VMD_0_MDS_0.ref
                ) {
                    type(ACTIVATE_OPERATION_1) {
                        conceptDescription(EN_US) {
                            "Accepts three arguments which are concatenated and written to the metric value of " +
                                    "the operation target."
                        }
                    }

                    safetyClassification(SafetyClassification.MED_A)

                    argument(
                        name = ACTIVATE_OPERATION_ARGUMENT_1,
                        type = XSD_STRING
                    )
                    argument(
                        name = ACTIVATE_OPERATION_ARGUMENT_2,
                        type = XSD_DECIMAL
                    )
                    argument(
                        name = ACTIVATE_OPERATION_ARGUMENT_3,
                        type = XSD_ANY_URI
                    )
                }
            }

            systemContext(Handles.SYSTEM_CONTEXT_MDS_0) {
                patientContext(Handles.PATIENT_CONTEXT_MDS_0)
                locationContext(Handles.LOCATION_CONTEXT_MDS_0)
            }

            clock(Handles.CLOCK_MDS_0) {
                timeProtocol(CFC_532224) {
                    conceptDescription(EN_US) { "None" }
                }
                timeProtocol(CFC_532225) {
                    conceptDescription(EN_US) { "NTPv4" }
                }
                timeProtocol(CFC_532234) {
                    conceptDescription(EN_US) { "EBWW" }
                }
            }

            battery(Handles.BATTERY_0_MDS_0) {
                capacityFullCharge(decimal(100), MDC_DIM_X_AMP_HR) {
                    conceptDescription(EN_US) { "Magnitude ampere(s) hour" }
                }

                voltageSpecified(decimal(230), MDC_DIM_X_VOLT) {
                    conceptDescription(EN_US) { "Magnitude volt(s)" }
                }
            }

            vmd(Handles.VMD_0_MDS_0) {
                type(SAMPLE_VMD_1) {
                    conceptDescription(EN_US) {
                        "SDPi Test VMD that contains settings and measurements including waveforms"
                    }
                }

                channel(Handles.CHANNEL_0_VMD_0_MDS_0) {
                    type(SAMPLE_CHANNEL_1) {
                        conceptDescription(EN_US) { "Channel that contains settings" }
                    }

                    numericMetric(
                        handle = Handles.NUMERIC_METRIC_0_CHANNEL_0_VMD_0_MDS_0,
                        metricCategory = MetricCategory.SETTING,
                        metricAvailability = MetricAvailability.INTERMITTENT,
                        resolution = decimal("0.1"),
                        unit = codedValue(MDC_DIM_DIMLESS) {
                            conceptDescription(EN_US) { "Dimensionless" }
                        }
                    ) {
                        type(SAMPLE_METRIC_1) {
                            conceptDescription(EN_US) { "Numeric setting, externally controllable" }
                        }

                        technicalRange {
                            lower(decimal(1))
                            upper(decimal(100))
                            stepWidth(decimal(1))
                        }
                    }

                    enumStringMetric(
                        handle = Handles.ENUM_STRING_METRIC_0_CHANNEL_0_VMD_0_MDS_0,
                        metricCategory = MetricCategory.SETTING,
                        metricAvailability = MetricAvailability.INTERMITTENT,
                        unit = codedValue(MDC_DIM_DIMLESS) {
                            conceptDescription(EN_US) { "Dimensionless" }
                        }
                    ) {
                        type(SAMPLE_METRIC_2) {
                            conceptDescription(EN_US) { "Enum setting, externally controllable" }
                        }

                        allowedValue("ON") {
                            type(ENUM_VALUE_ON) {
                                conceptDescription(EN_US) { "Enum Value ON" }
                            }
                        }
                        allowedValue("OFF") {
                            type(ENUM_VALUE_OFF) {
                                conceptDescription(EN_US) { "Enum Value OFF" }
                            }
                        }
                        allowedValue("STANDBY") {
                            type(ENUM_VALUE_STANDBY) {
                                conceptDescription(EN_US) { "Enum Value STANDBY" }
                            }
                        }
                    }

                    stringMetric(
                        handle = Handles.STRING_METRIC_0_CHANNEL_0_VMD_0_MDS_0,
                        metricCategory = MetricCategory.SETTING,
                        metricAvailability = MetricAvailability.INTERMITTENT,
                        unit = codedValue(MDC_DIM_DIMLESS) {
                            conceptDescription(EN_US) { "Dimensionless" }
                        }
                    ) {
                        type(SAMPLE_METRIC_3) {
                            conceptDescription(EN_US) { "String setting" }
                        }
                    }

                    stringMetric(
                        handle = Handles.STRING_METRIC_1_CHANNEL_0_VMD_0_MDS_0,
                        metricCategory = MetricCategory.SETTING,
                        metricAvailability = MetricAvailability.INTERMITTENT,
                        unit = codedValue(MDC_DIM_DIMLESS) {
                            conceptDescription(EN_US) { "Dimensionless" }
                        }
                    ) {
                        type(METRIC_TARGETED_BY_ACTIVATE_OPERATION_2) {
                            conceptDescription(EN_US) { "Operation target of the activate operation" }
                        }
                    }
                }

                channel(Handles.CHANNEL_1_VMD_0_MDS_0) {
                    type(SAMPLE_CHANNEL_2) {
                        conceptDescription(EN_US) { "Channel that contains measurements" }
                    }

                    numericMetric(
                        handle = Handles.NUMERIC_METRIC_1_CHANNEL_1_VMD_0_MDS_0,
                        metricCategory = MetricCategory.MEASUREMENT,
                        metricAvailability = MetricAvailability.INTERMITTENT,
                        resolution = decimal("0.1"),
                        unit = codedValue(MDC_DIM_DIMLESS) {
                            conceptDescription(EN_US) { "Dimensionless" }
                        }
                    ) {
                        type(SAMPLE_METRIC_4) {
                            conceptDescription(EN_US) { "Periodically determined intermittent numeric measurement metric" }
                        }

                        determinationPeriod(5.seconds)

                        technicalRange {
                            lower(decimal(1))
                            upper(decimal(100))
                            stepWidth(decimal(1))
                        }
                    }

                    realTimeSampleArrayMetric(
                        handle = Handles.RTSA_METRIC_0_CHANNEL_1_VMD_0_MDS_0,
                        metricCategory = MetricCategory.MEASUREMENT,
                        metricAvailability = MetricAvailability.CONTINUOUS,
                        resolution = decimal("1"),
                        samplePeriod = 10.milliseconds,
                        unit = codedValue(MDC_DIM_DIMLESS) {
                            conceptDescription(EN_US) { "Dimensionless" }
                        }
                    ) {
                        type(WAVEFORM_METRIC_1) {
                            conceptDescription(EN_US) { "Waveform metric 1" }
                        }
                    }

                    realTimeSampleArrayMetric(
                        handle = Handles.RTSA_METRIC_1_CHANNEL_1_VMD_0_MDS_0,
                        metricCategory = MetricCategory.MEASUREMENT,
                        metricAvailability = MetricAvailability.CONTINUOUS,
                        resolution = decimal("1"),
                        samplePeriod = 10.milliseconds,
                        unit = codedValue(MDC_DIM_DIMLESS) {
                            conceptDescription(EN_US) { "Dimensionless" }
                        }
                    ) {
                        type(WAVEFORM_METRIC_2) {
                            conceptDescription(EN_US) { "Waveform metric 2" }
                        }
                    }

                    realTimeSampleArrayMetric(
                        handle = Handles.RTSA_METRIC_2_CHANNEL_1_VMD_0_MDS_0,
                        metricCategory = MetricCategory.MEASUREMENT,
                        metricAvailability = MetricAvailability.CONTINUOUS,
                        resolution = decimal("1"),
                        samplePeriod = 10.milliseconds,
                        unit = codedValue(MDC_DIM_DIMLESS) {
                            conceptDescription(EN_US) { "Dimensionless" }
                        }
                    ) {
                        type(WAVEFORM_METRIC_4) {
                            conceptDescription(EN_US) { "Waveform metric 3" }
                        }
                    }

                    realTimeSampleArrayMetric(
                        handle = Handles.RTSA_METRIC_3_CHANNEL_1_VMD_0_MDS_0,
                        metricCategory = MetricCategory.MEASUREMENT,
                        metricAvailability = MetricAvailability.CONTINUOUS,
                        resolution = decimal("1"),
                        samplePeriod = 500.milliseconds,
                        unit = codedValue(MDC_DIM_DIMLESS) {
                            conceptDescription(EN_US) { "Dimensionless" }
                        }
                    ) {
                        type(WAVEFORM_METRIC_3) {
                            conceptDescription(EN_US) { "Waveform metric 4" }
                        }
                    }
                }
            }

            vmd(Handles.VMD_1_MDS_0) {
                type(SAMPLE_VMD_2) {
                    conceptDescription(EN_US) {
                        "SDPi Test VMD that contains settings to be externally controlled by bulk operations"
                    }
                }

                sco(Handles.SCO_VMD1_MDS_0) {
                    setMetricStateOperation(
                        handle = Handles.SET_METRIC_0_SCO_VMD_1_MDS_0,
                        operationTarget = Handles.CHANNEL_0_VMD_1_MDS_0.ref
                    ) {
                        type(SET_MULTIPLE_NUMERIC_METRIC_VALUE) {
                            conceptDescription(EN_US) { "Sets the @Value of 2 metric states at once" }
                        }

                        safetyClassification(SafetyClassification.MED_A)
                    }
                }

                channel(Handles.CHANNEL_0_VMD_1_MDS_0) {
                    type(SAMPLE_CHANNEL_1) {
                        conceptDescription(EN_US) {
                            "Channel that contains settings to be externally controlled by bulk operations"
                        }
                    }

                    numericMetric(
                        handle = Handles.NUMERIC_METRIC_0_CHANNEL_0_VMD_1_MDS_0,
                        metricCategory = MetricCategory.SETTING,
                        metricAvailability = MetricAvailability.INTERMITTENT,
                        resolution = decimal(1),
                        unit = codedValue(MDC_DIM_DIMLESS) {
                            conceptDescription(EN_US) { "Dimensionless" }
                        }
                    ) {
                        type(SAMPLE_METRIC_5) {
                            conceptDescription(EN_US) { "Numeric setting, externally controllable by bulk update" }
                        }
                    }

                    numericMetric(
                        handle = Handles.NUMERIC_METRIC_1_CHANNEL_0_VMD_1_MDS_0,
                        metricCategory = MetricCategory.SETTING,
                        metricAvailability = MetricAvailability.INTERMITTENT,
                        resolution = decimal(1),
                        unit = codedValue(MDC_DIM_DIMLESS) {
                            conceptDescription(EN_US) { "Dimensionless" }
                        }
                    ) {
                        type(SAMPLE_METRIC_6) {
                            conceptDescription(EN_US) { "Numeric setting, externally controllable by bulk update" }
                        }
                    }
                }
            }
        }

        mds(Handles.MDS_1) {
            type(SAMPLE_MDS_2) {
                conceptDescription(EN_US) {
                    "SDPi Test MDS used for description modification reports. This MDS periodically inserts and " +
                            "deletes a VMD including Channels including Metrics."
                }
            }

            vmd(Handles.VMD_0_MDS_1) {
                type(SAMPLE_VMD_1) {
                    conceptDescription(EN_US) {
                        "SDPi Test VMD that contains a metric and an alarm for which units and cause-remedy " +
                                "information is periodically updated (description updates)"
                    }
                }

                alertSystem(Handles.ALERT_SYSTEM_VMD_0_MDS_1) {
                    alertCondition(
                        handle = Handles.ALERT_CONDITION_0_VMD_0_MDS_1,
                        kind = AlertConditionKind.OTHER,
                        priority = AlertConditionPriority.NONE
                    ) {
                        type(SAMPLE_ALERT_CONDITION_2) {
                            conceptDescription(EN_US) {
                                "An alert condition that periodically changes its cause-remedy information at " +
                                        "least every 5 seconds"
                            }
                        }

                        source(Handles.NUMERIC_METRIC_0_CHANNEL_0_VMD_0_MDS_1.ref)

                        causeInfo {
                            description(EN_US) { "Cause Info" }
                            remedyInfo {
                                description(EN_US) { "Remedy Info" }
                            }
                        }
                    }
                }

                channel(Handles.CHANNEL_0_VMD_0_MDS_1) {
                    type(SAMPLE_CHANNEL_3) {
                        conceptDescription(EN_US) {
                            "Channel that contains a metric which is periodically changing its unit of measure"
                        }
                    }

                    numericMetric(
                        handle = Handles.NUMERIC_METRIC_0_CHANNEL_0_VMD_0_MDS_1,
                        metricCategory = MetricCategory.SETTING,
                        metricAvailability = MetricAvailability.INTERMITTENT,
                        resolution = decimal(1),
                        unit = MDC_DIM_MILLI_L_PER_HR
                    ) {
                        type(MDC_FLOW_FLUID_PUMP) {
                            conceptDescription(EN_US) {
                                "Flow Rate: Numeric measurement that periodically changes the unit of measure at " +
                                        "least every 5 seconds"
                            }
                        }
                    }
                }
            }
        }
    }
}