package org.recap.model.export;

import lombok.Data;

import java.util.List;

/**
 * Created by premkb on 19/8/16.
 */
@Data
public class DataDumpRequest {

    private Integer etlRequestId;

    private String fetchType;
    private String date;
    private String toDate;
    private String transmissionType;
    private String requestingInstitutionCode;
    private String toEmailAddress;
    private String outputFileFormat;
    private String dateTimeString;
    private String requestId;
    private String userName;

    private boolean isRecordsAvailable;
    private boolean isIncrementalSequence;
    private boolean isRequestFromSwagger;

    private List<Integer> collectionGroupIds;
    private List<String> institutionCodes;
    private List<String> imsDepositoryCodes;
}
