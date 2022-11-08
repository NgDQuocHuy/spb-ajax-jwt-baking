package com.quochuy.service.transfer;

import com.quochuy.model.Transfer;
import com.quochuy.model.dto.TransferHistoryDTO;
import com.quochuy.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TransferServiceImpl implements ITransferService {

    @Autowired
    private TransferRepository transferRepository;

    @Override
    public List<Transfer> findAll() {
        return transferRepository.findAll();
    }

    @Override
    public List<TransferHistoryDTO> getAllHistories() {
        return transferRepository.getAllHistories();
    }

    @Override
    public Transfer getById(Long id) {
        return null;
    }

    @Override
    public Optional<Transfer> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public BigDecimal getSumFeesAmount() {
        return transferRepository.getSumFeesAmount();
    }

    @Override
    public Transfer save(Transfer transfer) {
        return null;
    }

    @Override
    public void remove(Long id) {

    }
}
