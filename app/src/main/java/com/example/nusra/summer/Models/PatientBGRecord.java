package com.example.nusra.summer.Models;

import java.sql.Time;
import java.time.ZonedDateTime;

public class PatientBGRecord {
    public String GlucoseLevel;
    public String RecordOfDate;
    public String RecordOfTime;
    public String RecordOfMealBefore;
    public String GlucoseLevelMetric;

    public PatientBGRecord() {
    }

    public PatientBGRecord(String glucoseLevel, String recordOfTime, String recordOfMealBefore, String glucoseLevelMetric, String recordOfDate ) {
        this.GlucoseLevel = glucoseLevel;
        this.RecordOfTime = recordOfTime;
        this.RecordOfMealBefore = recordOfMealBefore;
        this.GlucoseLevelMetric = glucoseLevelMetric;
        this.RecordOfDate = recordOfDate;
    }

}

