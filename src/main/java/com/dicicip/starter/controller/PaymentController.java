package com.dicicip.starter.controller;

import com.dicicip.starter.model.Payment;
import com.dicicip.starter.model.Product;
import com.dicicip.starter.repository.PaymentRepository;
import com.dicicip.starter.repository.ProductRepository;
import com.dicicip.starter.util.APIResponse;
import com.dicicip.starter.util.file.FileUtil;
import com.dicicip.starter.util.query.DB;
import com.dicicip.starter.util.query.QueryHelpers;
import com.dicicip.starter.util.validator.Validator;
import com.dicicip.starter.util.validator.ValidatorItem;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
@PreAuthorize("isFullyAuthenticated()")
public class PaymentController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PaymentRepository repository;

    @Autowired
    DB db;

    @Autowired
    FileUtil fileUtil;

    @RequestMapping(method = RequestMethod.GET)
    public APIResponse<?> getAll(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return new APIResponse<>(QueryHelpers.getData(request.getParameterMap(), "payments", db));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}/detail")
    public APIResponse<?> getOne(
            @PathVariable("id") Long id,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Optional<Payment> payment = this.repository.findById(id);

        if (payment.isPresent()) {
            return new APIResponse<>(payment.get());
        } else {
            response.setStatus(400);
            return new APIResponse<>(
                    null,
                    false,
                    "Failed get data"
            );
        }
    }

    @RequestMapping(method = RequestMethod.POST, path = "/store")
    @Transactional(rollbackFor = Exception.class)
    public APIResponse<?> store(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody Payment requestBody
    ) {

        Validator<Payment> validator = new Validator<>(
                requestBody,
                new ValidatorItem("receiver_name", "required"),
                new ValidatorItem("receiver_address", "required"),
                new ValidatorItem("payment_method", "required"),
                new ValidatorItem("product_id", "required"),
                new ValidatorItem("qty", "required"),
                new ValidatorItem("note")
        );

        if (validator.valid()) {

            Optional<Product> product = productRepository.findById(requestBody.product_id.longValue());

            if (!product.isPresent()) {
                response.setStatus(400);
                return new APIResponse<>(
                        null,
                        false,
                        "Failed save data, product not found"
                );
            }

            /*Validate Payment Method Value*/
            if (!EnumUtils.isValidEnum(Payment.PaymentMethod.class, requestBody.payment_method)) {
                response.setStatus(400);
                return new APIResponse<>(
                        null,
                        false,
                        "Failed save data, payment method unidentified"
                );
            }

            Payment payment = new Payment();
            payment.invoice_number = "INV-" + (new Date().getTime());

            payment.payment_method = requestBody.payment_method;
            payment.status = Payment.PaymentStatus.pending.name();
            payment.receiver_name = requestBody.receiver_name;
            payment.receiver_address = requestBody.receiver_address;
            payment.product_id = product.get().id.intValue();
            payment.product_price = product.get().price;
            payment.qty = requestBody.qty;
            payment.total = payment.product_price * payment.qty;
            payment.note = requestBody.note;

            return new APIResponse<>(repository.save(payment));
        } else {
            response.setStatus(400);
            return new APIResponse<>(
                    validator.getErrorsList(),
                    false,
                    "Failed save data"
            );
        }

    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}/update-status")
    @Transactional(rollbackFor = Exception.class)
    public APIResponse<?> updateStatus(
            @PathVariable("id") Long id,
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody HashMap<String, Object> requestBody
    ) {

        Validator<HashMap<String, Object>> validator = new Validator<>(
                requestBody,
                new ValidatorItem("status", "required"),
                new ValidatorItem("proof_of_payment")
        );

        if (validator.valid()) {

            /*Validate Payment Method Value*/
            if (!EnumUtils.isValidEnum(Payment.PaymentStatus.class, String.valueOf(requestBody.get("status")))) {
                response.setStatus(400);
                return new APIResponse<>(
                        null,
                        false,
                        "Failed save data, payment status unidentified"
                );
            }

            Optional<Payment> payment = this.repository.findById(id);

            if (payment.isPresent()) {

                Payment p = payment.get();

                p.status = String.valueOf(requestBody.get("status"));
                if (p.status.equals(Payment.PaymentStatus.lunas.name())) {
                    p.paid_at = new Timestamp(new Date().getTime());
                }

                p = this.repository.save(p);

                return new APIResponse<>(p);

            } else {
                response.setStatus(400);
                return new APIResponse<>(
                        null,
                        false,
                        "Failed get data"
                );
            }

        } else {
            response.setStatus(400);
            return new APIResponse<>(
                    validator.getErrorsList(),
                    false,
                    "Failed save data"
            );
        }

    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}/delete")
    @Transactional(rollbackFor = Exception.class)
    public APIResponse<?> delete(
            @PathVariable("id") Long id,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Optional<Payment> payment = this.repository.findById(id);

        if (payment.isPresent()) {

            this.repository.delete(payment.get());

            return new APIResponse<>(null);

        } else {
            response.setStatus(400);
            return new APIResponse<>(
                    null,
                    false,
                    "Failed delete data"
            );
        }
    }

}
