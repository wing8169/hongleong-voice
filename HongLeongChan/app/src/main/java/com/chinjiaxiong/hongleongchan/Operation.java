package com.chinjiaxiong.hongleongchan;

//{
//    "status": "succeeded",
//    "createdDateTime": "2018-10-06T16:03:59.3168904Z",
//    "lastActionDateTime": "2018-10-06T16:04:01.0767985Z",
//    "processingResult": {
//        "identifiedProfileId": "00000000-0000-0000-0000-000000000000",
//        "confidence": "High"
//    }
//}
public class Operation {
    private String status;
    private String createdDateTime;
    private String lastActionDateTime;
    private ProcessingResult processingResult;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getLastActionDateTime() {
        return lastActionDateTime;
    }

    public void setLastActionDateTime(String lastActionDateTime) {
        this.lastActionDateTime = lastActionDateTime;
    }

    public ProcessingResult getProcessingResult() {
        return processingResult;
    }

    public void setProcessingResult(ProcessingResult processingResult) {
        this.processingResult = processingResult;
    }
}

class ProcessingResult{
    public String getIdentifiedProfileId() {
        return identifiedProfileId;
    }

    public void setIdentifiedProfileId(String identifiedProfileId) {
        this.identifiedProfileId = identifiedProfileId;
    }

    public String getConfidence() {
        return confidence;
    }

    public void setConfidence(String confidence) {
        this.confidence = confidence;
    }

    private String identifiedProfileId;
    private String confidence;
}