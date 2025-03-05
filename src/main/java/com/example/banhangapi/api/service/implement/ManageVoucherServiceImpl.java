package com.example.banhangapi.api.service.implement;

import com.example.banhangapi.api.dto.RequestCreateVoucherDTO;
import com.example.banhangapi.api.dto.VoucherDTO;
import com.example.banhangapi.api.entity.Voucher;
import com.example.banhangapi.api.mapper.VoucherMapper;
import com.example.banhangapi.api.repository.VoucherRepository;
import com.example.banhangapi.api.service.ManagerVoucherService;
import com.example.banhangapi.helper.handleException.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.security.SecureRandom;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ManageVoucherServiceImpl implements ManagerVoucherService {
    private final VoucherRepository voucherRepository;
    private final VoucherMapper voucherMapper;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();


    @Override
    @SneakyThrows
    public VoucherDTO createNewVoucher(RequestCreateVoucherDTO voucher){
        LocalDate startDate = convertToLocalDate(voucher.getStartDate());
        LocalDate expireDate = convertToLocalDate(voucher.getExpirationDate());
        if(startDate.isAfter(expireDate)){
            throw new RuntimeException("exroot");
        }
        String codeVoucher = voucher.getVoucherCode() != null ? convertToUpperCase(voucher.getVoucherCode()) : generateRandomString();
        Voucher newVoucher = new Voucher();
        newVoucher.setDescription(voucher.getDescription());
        float discount = ((float) voucher.getDiscount()) / 100;
        newVoucher.setLimitSlot(voucher.getLimitSlot());
        newVoucher.setDiscount(discount);
        newVoucher.setVoucherCode(codeVoucher);
        newVoucher.setLimitedUsage(voucher.isLimitedUsage());
        newVoucher.setStartDate(startDate);
        newVoucher.setExpirationDate(expireDate);
        return  voucherMapper.toVoucherDTO(voucherRepository.save(newVoucher));

    };
    public VoucherDTO getInfoVoucher(String voucherCode){
        Voucher voucher = voucherRepository.findByVoucherCode(voucherCode).orElseThrow(()->new ProductNotFoundException("Voucher Not Found"));
        VoucherDTO voucherDTO = voucherMapper.toVoucherDTO(voucher);
        voucherDTO.setRemainingTime(calculateTimeRemaining(voucher.getExpirationDate()));
        return voucherDTO;
    };
    public List<VoucherDTO> getListVoucherHaveSlotLimit(){
        List<Voucher> vouchers = voucherRepository.findAllByLimitSlotGreaterThan(0);
        return vouchers.stream().map(voucherMapper::toVoucherDTO).collect(Collectors.toList());
    };
    public Page<VoucherDTO> getAllVoucher(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return voucherRepository.findAll(pageable).map(voucherMapper::toVoucherDTO);
    };
    public  int userUserVoucher(String voucherId){
        return 0;
    };
    private String generateRandomString() {
        int length = 5 + RANDOM.nextInt(4); // Độ dài ngẫu nhiên từ 5 đến 10
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return convertToUpperCase(sb.toString());
    }
    private String convertToUpperCase(String input) {
        if (input == null) {
            return null;
        }
        return input.toUpperCase();
    }
    private boolean isValidDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
    private LocalDate convertToLocalDate(String dateStr) {
        // Định dạng ngày
        if (!isValidDate(dateStr)) {
            throw new ProductNotFoundException("Date Format Error");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        try {
            // Chuyển chuỗi ngày thành LocalDate
            return LocalDate.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            // Xử lý lỗi nếu ngày không hợp lệ
            System.out.println("Invalid date format: " + dateStr);
            return null;
        }
    }
    private static String calculateTimeRemaining(LocalDate expirationDate) {
        LocalDate today = LocalDate.now();
        LocalDate expirationDateTime = expirationDate;

        if (today.isEqual(expirationDate)) {
            LocalTime now = LocalTime.now();
            LocalTime endOfDay = LocalTime.of(23, 59, 59);
            long hoursRemaining = ChronoUnit.HOURS.between(now, endOfDay);
            long minutesRemaining = ChronoUnit.MINUTES.between(now, endOfDay) % 60;

            return String.format("%d hours, %d minutes.", hoursRemaining, minutesRemaining);
        }


        long daysRemaining = ChronoUnit.DAYS.between(today, expirationDateTime);

        if (daysRemaining < 0) {
            return null;
        }

        // Trả về kết quả dạng chuỗi
        return String.format("%d days.", daysRemaining);
    }
}
