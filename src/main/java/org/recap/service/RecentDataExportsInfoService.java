package org.recap.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.recap.RecapCommonConstants;
import org.recap.model.export.S3RecentDataExportInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

@Service
public class RecentDataExportsInfoService {

    private static final Logger logger = LoggerFactory.getLogger(RecentDataExportsInfoService.class);

    @Autowired
    AmazonS3 s3client;

    @Value("${scsbBucketName}")
    private String scsbBucketName;

    @Value("${s3.data.dump.dir}")
    private String s3DataExportBasePath;

    public List<S3RecentDataExportInfo> generateRecentDataExportsInfo(String institution, String bibDataFormat) {

        List<S3RecentDataExportInfo> recentDataExportInfoList = new ArrayList<>();
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        try {
            listObjectsRequest.setBucketName(scsbBucketName);
            listObjectsRequest.setPrefix(s3DataExportBasePath + institution + "/" + bibDataFormat + "Xml/Full/" + institution);
            ObjectListing objectListing = s3client.listObjects(listObjectsRequest);
            for (S3ObjectSummary os : objectListing.getObjectSummaries()) {
                Map<String, String> records = getObjectContent(os.getKey());
                S3RecentDataExportInfo s3RecentDataExportInfo = new S3RecentDataExportInfo();
                s3RecentDataExportInfo.setKeyName(os.getKey());
                s3RecentDataExportInfo.setInstitution(institution);
                s3RecentDataExportInfo.setBibDataFormat(bibDataFormat);
                s3RecentDataExportInfo.setGcd(records.get("Collection Group Id(s)"));
                s3RecentDataExportInfo.setBibCount(records.get("No of Bibs Exported"));
                s3RecentDataExportInfo.setItemCount(records.get("No of Items Exported"));
                s3RecentDataExportInfo.setKeySize(os.getSize());
                s3RecentDataExportInfo.setKeyLastModified(os.getLastModified());
                recentDataExportInfoList.add(s3RecentDataExportInfo);
                logger.info("File with the key -->" + os.getKey() + " " + os.getSize() + " " + os.getLastModified());
            }
        } catch (Exception e) {
            logger.error(RecapCommonConstants.LOG_ERROR, e);
        }
        recentDataExportInfoList.sort(Comparator.comparing(S3RecentDataExportInfo::getKeyLastModified).reversed());
        return recentDataExportInfoList.stream().limit(3).collect(Collectors.toList());
    }

    public Map<String, String> getObjectContent(String fileName) {
        List<String> str = new ArrayList<>();
        String[] records = null;
        String[] headers = null;
        try {
            String basepath = fileName.substring(0, fileName.lastIndexOf('/'));
            String csvFileName = fileName.substring(fileName.lastIndexOf('/'));
            csvFileName = csvFileName.substring(1, csvFileName.lastIndexOf('.'));
            S3Object s3Object = s3client.getObject(scsbBucketName, basepath + "/" + "ExportDataDump_Full_" + csvFileName + ".csv");
            S3ObjectInputStream inputStream = s3Object.getObjectContent();
            Scanner fileIn = new Scanner(inputStream);
            if (null != fileIn) {
                while (fileIn.hasNext()) {
                    str.add(fileIn.nextLine());
                }
                headers = str.get(0).replace("\"", "").split(",");
                records = str.get(1).replace("\"", "").split(",");
            }
        } catch (Exception e) {
            logger.error(RecapCommonConstants.LOG_ERROR, e);
        }
        return mapResult(headers, records);
    }
    private Map<String, String> mapResult(String[] headers,
                                          String[] records) {
        Map<String, String> result = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            result.put(headers[i], records[i]);
        }
        return result;
    }
}