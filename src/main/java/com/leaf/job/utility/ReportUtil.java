package com.leaf.job.utility;

import com.leaf.job.dto.ReportDTO;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Component
public class ReportUtil implements ServletContextAware {


    @Autowired
    ApplicationContext appContext;

    private ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext arg0) {
        servletContext = arg0;
    }


    public void createReportDownload(HttpServletResponse response, ReportDTO reportDTO) throws Exception {

        // Get report path
        String path = reportDTO.getReportPath() + reportDTO.getReportName();
        //String logo = servletContext.getRealPath("/") + "/WEB-INF/classes/images/policecooplogo.jpg";


        // Get download file name
        String downloadFileName = reportDTO.getDownloadName() + ".pdf";

        // Compile report
        JasperReport jasperReport = null;
        try {
            final InputStream stream = this.getClass().getResourceAsStream(path);
            jasperReport = JasperCompileManager.compileReport(stream);

            // Set DTO list
            JRDataSource jRdataSource = new JREmptyDataSource();

            if (reportDTO.getDtoList() != null) {
                jRdataSource = new JRBeanCollectionDataSource(reportDTO.getDtoList());
            }

            // Set parameters
            Map<String, Object> parameters = new HashMap<>();



            // Fill report
            JasperPrint jasperPrint = null;

            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jRdataSource);


            response.setHeader("Content-Disposition", "attachment; filename=" + downloadFileName);
            response.setContentType("application/pdf");
            OutputStream outputStream = response.getOutputStream();

            // Export report to PDF
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        } catch (JRException | IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
