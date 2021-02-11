package org.recap.controller;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCaseUT;
import org.recap.RecapConstants;
import org.recap.TestUtil;
import org.recap.camel.dynamicrouter.DynamicRouteBuilder;
import org.recap.repository.InstitutionDetailsRepository;
import org.recap.service.executor.datadump.DataDumpSchedulerExecutorService;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class DataDumpSequenceRestControllerUT extends BaseTestCaseUT {
    @InjectMocks
    DataDumpSequenceRestController dataDumpSequenceRestController;

    @Mock
    InstitutionDetailsRepository institutionDetailsRepository;

    @Mock
    DynamicRouteBuilder dynamicRouteBuilder;

    @Mock
    DataDumpSchedulerExecutorService dataDumpSchedulerExecutorService;


    @Test
    public void testExportDataDump() {
        Mockito.when(institutionDetailsRepository.findAllInstitutionCodeExceptHTC()).thenReturn(TestUtil.getInstitutionCodeExceptHTC());
        Mockito.when(dataDumpSchedulerExecutorService.initiateDataDumpForScheduler(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(RecapConstants.DATADUMP_PROCESS_STARTED);
        String exportDataDump = dataDumpSequenceRestController.exportDataDump(new Date().toString());
        assertEquals(RecapConstants.DATADUMP_PROCESS_STARTED, exportDataDump);
    }
}
