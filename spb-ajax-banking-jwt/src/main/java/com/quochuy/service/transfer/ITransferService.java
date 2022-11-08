package com.quochuy.service.transfer;

import com.quochuy.model.Transfer;
import com.quochuy.model.dto.TransferHistoryDTO;
import com.quochuy.service.IGeneralService;

import java.math.BigDecimal;
import java.util.List;

public interface ITransferService extends IGeneralService<Transfer> {

    List<TransferHistoryDTO> getAllHistories();

    BigDecimal getSumFeesAmount();
}
