package com.natsukashiiz.iiserverapi.service;

import com.natsukashiiz.iiboot.configuration.jwt.UserDetailsImpl;
import com.natsukashiiz.iicommon.model.Pagination;
import com.natsukashiiz.iicommon.model.Result;
import com.natsukashiiz.iicommon.utils.CommonUtil;
import com.natsukashiiz.iicommon.utils.MapperUtil;
import com.natsukashiiz.iicommon.utils.ResponseUtil;
import com.natsukashiiz.iiserverapi.model.response.SignHistoryResponse;
import com.natsukashiiz.iiserverapi.repository.SignedHistoryRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SignHistoryService {

    @Resource
    private SignedHistoryRepository signedHistoryRepository;

    public Result<?> getAll(UserDetailsImpl auth, Pagination paginate) {
        Pageable pageable = CommonUtil.getPaginate(paginate);
        Long count = this.signedHistoryRepository.countByUid(auth.getId());
        List<com.natsukashiiz.iiserverapi.entity.SignHistory> histories = this.signedHistoryRepository.findByUid(auth.getId(), pageable);
        List<SignHistoryResponse> response = MapperUtil.mapList(histories, SignHistoryResponse.class);
        return ResponseUtil.successList(response, count);
    }
}
