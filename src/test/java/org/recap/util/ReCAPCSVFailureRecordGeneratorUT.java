package org.recap.util;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.recap.BaseTestCaseUT;
import org.recap.RecapConstants;
import org.recap.model.csv.FailureReportReCAPCSVRecord;
import org.recap.model.jparw.ReportDataEntity;
import org.recap.model.jparw.ReportEntity;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ReCAPCSVFailureRecordGeneratorUT extends BaseTestCaseUT {

    @InjectMocks
    ReCAPCSVFailureRecordGenerator reCAPCSVFailureRecordGenerator;

    @Test
    public void prepareFailureReportReCAPCSVRecordInnerException() {
        ReportEntity reportEntity = getReportEntity();
        FailureReportReCAPCSVRecord failureReportReCAPCSVRecord = reCAPCSVFailureRecordGenerator.prepareFailureReportReCAPCSVRecord(reportEntity);
        assertNotNull(failureReportReCAPCSVRecord);
    }

    @Test
    public void getGetterMethod() {
        Method method = reCAPCSVFailureRecordGenerator.getGetterMethod(RecapConstants.HEADER_FETCH_TYPE);
        assertNull(method);
    }

    private ReportEntity getReportEntity() {
        ReportEntity reportEntity = new ReportEntity();
        ReportDataEntity reportDataEntity = new ReportDataEntity();
        reportDataEntity.setHeaderName(RecapConstants.HEADER_FETCH_TYPE);
        reportDataEntity.setHeaderValue(RecapConstants.DATADUMP_FETCHTYPE_INCREMENTAL);
        reportEntity.setReportDataEntities(Arrays.asList(reportDataEntity));
        reportEntity.setCreatedDate(new Date());
        return reportEntity;
    }
}
