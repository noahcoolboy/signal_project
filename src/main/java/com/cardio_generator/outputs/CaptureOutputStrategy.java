package com.cardio_generator.outputs;

import java.util.ArrayList;
import java.util.List;

import com.data_management.PatientRecord;

public class CaptureOutputStrategy implements OutputStrategy {

    private final List<PatientRecord> outputs = new ArrayList<>();

    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        double value = 0;
        if ("Alert".equals(label)) {
            if ("triggered".equals(data)) {
                value = 1.0;
            } else if ("resolved".equals(data)) {
                value = 0.0;
            } else {
                value = -1.0; // Unknown state
            }
        } else if ("Saturation".equals(label) && data.contains("%")) {
            value = Double.parseDouble(data.substring(0, data.indexOf("%")));
        } else {
            try {
                value = Double.parseDouble(data);
            } catch (NumberFormatException e) {
                // Use default value of 0 for unparseable data
            }
        }
        
        outputs.add(new PatientRecord(patientId, value, label, timestamp));
    }

    public List<PatientRecord> getOutputs() {
        return outputs;
    }

    public void clear() {
        outputs.clear();
    }
}
