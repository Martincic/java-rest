/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.rit.croatia.companydataserver.businesslayer;

import companydata.Timecard;

/**
 *
 * This class is used to convert json to Timecard object,
 * Because gson can only detect and assign primitives (Timestamp issuess)
 */
public class TimecardJson {
    
    public int timecard_id;
    public String start_time;
    public String end_time;
    public int emp_id;
    
    public TimecardJson() {}
}
