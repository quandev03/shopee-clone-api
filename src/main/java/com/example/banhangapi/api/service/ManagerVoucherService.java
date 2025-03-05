package com.example.banhangapi.api.service;

import com.example.banhangapi.api.dto.RequestCreateVoucherDTO;
import com.example.banhangapi.api.dto.VoucherDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface  ManagerVoucherService {
    VoucherDTO createNewVoucher(RequestCreateVoucherDTO voucher);
    VoucherDTO getInfoVoucher(String voucherId);
    List<VoucherDTO> getListVoucherHaveSlotLimit();
    Page<VoucherDTO> getAllVoucher(int page, int size);
    int userUserVoucher(String voucherId);

}
