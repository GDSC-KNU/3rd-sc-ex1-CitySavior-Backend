package GDSCKNU.CitySavior.controller.report;

import GDSCKNU.CitySavior.annotation.HasFile;
import GDSCKNU.CitySavior.common.resolver.annotation.UserInfo;
import GDSCKNU.CitySavior.dto.report.request.ReportRequest;
import GDSCKNU.CitySavior.dto.report.response.MapReportsResponse;
import GDSCKNU.CitySavior.dto.report.response.ReportDetailResponse;
import GDSCKNU.CitySavior.dto.report.response.StatisticsResponse;
import GDSCKNU.CitySavior.dto.reportComment.request.CreateReportCommentRequest;
import GDSCKNU.CitySavior.entity.member.Member;
import GDSCKNU.CitySavior.entity.memberDetail.MemberDetailsImpl;
import GDSCKNU.CitySavior.service.ai.AIService;
import GDSCKNU.CitySavior.service.member.MemberService;
import GDSCKNU.CitySavior.service.report.ReportService;
import GDSCKNU.CitySavior.service.storage.StorageService;
import GDSCKNU.CitySavior.service.achieveMember.AchievementMemberService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Slf4j
public class ReportsController {
    private final StorageService storageService;
    private final AIService aiService;
    private final ReportService reportService;
    private final AchievementMemberService achievementMemberService;
    private final MemberService memberService;

    @GetMapping("/reports/{report_id}")
    public ReportDetailResponse reportDetail(@PathVariable("report_id") Long reportId) {
        return reportService.getReportDetail(reportId);
    }

    @GetMapping("/reports")
    public Map<String, List<MapReportsResponse>> getReportsByGIS(@RequestParam(name = "latitude") double latitude,
                                                                 @RequestParam(name = "longitude") double longitude,
                                                                 @RequestParam(name = "radius") int radius) {
        return reportService.getReportsByGIS(latitude, longitude, radius);
    }

    @GetMapping("/reports/statistics")
    public StatisticsResponse getStatistics() {
        return reportService.getStatistics();
    }

    @PostMapping("/reports")
    public Long report(@RequestPart(name = "imgFiles") @HasFile MultipartFile imgFiles,
                       @UserInfo MemberDetailsImpl memberDetails,
                       @RequestPart(name = "requestDto") @Valid ReportRequest requestDto) {

        String fileName = storageService.saveFile(imgFiles);
        int damageRate = aiService.evaluateDamageRate(imgFiles);
        achievementMemberService.updateAchievementRecord(requestDto.category(), memberDetails);
        Member member = memberService.findMemberByEmail(memberDetails.getUsername());
        return reportService.saveReport(requestDto, damageRate, fileName, member);
    }

    @PostMapping("/reports/{report_id}/comments")
    public Long addComment(@PathVariable("report_id") Long reportId, @RequestBody CreateReportCommentRequest content) {
        return reportService.addComment(reportId, content.comment());
    }

    @PatchMapping("/reports/{report_id}/end")
    public void endReport(@PathVariable("report_id") Long reportId) {
        reportService.endReport(reportId);
    }
}