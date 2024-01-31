package GDSCKNU.CitySavior.config;

import GDSCKNU.CitySavior.converter.ReportCommentsToCommentDtosConverter;
import GDSCKNU.CitySavior.converter.ReportToReportDetailResponseDtoConverter;
import GDSCKNU.CitySavior.converter.MapReportsToDtoConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new MapReportsToDtoConverter());
        registry.addConverter(new ReportToReportDetailResponseDtoConverter());
        registry.addConverter(new ReportCommentsToCommentDtosConverter());
    }
}
