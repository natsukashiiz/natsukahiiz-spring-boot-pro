package com.natsukashiiz.iiserverapi.service;

import com.natsukashiiz.iiboot.configuration.jwt.AuthPrincipal;
import com.natsukashiiz.iicommon.model.Http;
import com.natsukashiiz.iicommon.model.Pagination;
import com.natsukashiiz.iicommon.model.Result;
import com.natsukashiiz.iicommon.utils.CommonUtils;
import com.natsukashiiz.iicommon.utils.MapperUtils;
import com.natsukashiiz.iicommon.utils.ResultUtils;
import com.natsukashiiz.iiserverapi.entity.SignHistory;
import com.natsukashiiz.iiserverapi.entity.User;
import com.natsukashiiz.iiserverapi.model.response.SignHistoryResponse;
import com.natsukashiiz.iiserverapi.repository.SignHistoryRepository;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SignHistoryService {

    @Resource
    private SignHistoryRepository signHistoryRepository;

    public Result<?> getAll(AuthPrincipal auth, Pagination paginate) {
        Pageable pageable = CommonUtils.getPaginate(paginate);
        Long count = this.signHistoryRepository.countByUid(auth.getId());
        if (count == 0) {
            return ResultUtils.successEmpty();
        }
        List<SignHistory> histories = this.signHistoryRepository.findByUid(
                auth.getId(), pageable);
        List<SignHistoryResponse> response = MapperUtils.mapList(
                histories, SignHistoryResponse.class);
        return ResultUtils.successList(response, count);
    }

    public Result<?> save(HttpServletRequest httpRequest, AuthPrincipal auth) {
        Http http = CommonUtils.getHttp(httpRequest);

        // save sign history
        SignHistory history = new SignHistory();
        history.setUser(User.from(auth));
        history.setIpv4(http.getIpv4());
        history.setDevice(http.getDevice().value());
        history.setUa(http.getUa());
        this.signHistoryRepository.save(history);
        return ResultUtils.success();
    }
}
