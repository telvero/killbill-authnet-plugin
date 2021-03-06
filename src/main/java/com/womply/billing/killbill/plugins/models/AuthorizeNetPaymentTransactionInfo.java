/*
 *  Copyright 2016 Womply
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.womply.billing.killbill.plugins.models;

import com.womply.billing.killbill.plugins.db.MysqlAdapter;
import com.womply.billing.killbill.plugins.jooq.tables.records.AuthorizeNetTransactionsRecord;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.killbill.billing.catalog.api.Currency;
import org.killbill.billing.payment.api.TransactionType;
import org.killbill.billing.payment.plugin.api.PaymentPluginStatus;
import org.killbill.billing.plugin.api.payment.PluginPaymentTransactionInfoPlugin;

import java.util.Collections;
import java.util.UUID;

/**
 * Authorize.Net specific implementation of PluginPaymentTransactionInfoPlugin.
 */
public class AuthorizeNetPaymentTransactionInfo extends PluginPaymentTransactionInfoPlugin {

    public AuthorizeNetPaymentTransactionInfo(
            AuthorizeNetTransactionsRecord transactionResult) {
        super(
                UUID.fromString(transactionResult.getKbPaymentId()),
                UUID.fromString(transactionResult.getKbPaymentTransactionId()),
                TransactionType.valueOf(transactionResult.getKbTransactionType()),
                transactionResult.getAmount(),
                Currency.valueOf(transactionResult.getCurrency()),
                getPaymentPluginStatus(transactionResult),
                getGatewayError(transactionResult),
                getGatewayErrorCode(transactionResult),
                transactionResult.getAuthorizeNetTransactionId(),
                transactionResult.getAuthCode(),
                new DateTime(transactionResult.getCreatedAt(), DateTimeZone.UTC),
                new DateTime(transactionResult.getCreatedAt(), DateTimeZone.UTC),
                Collections.EMPTY_LIST);
    }

    protected static String getGatewayError(AuthorizeNetTransactionsRecord transactionResult) {
        if (MysqlAdapter.isTransactionSuccessful(transactionResult)) {
            return "";
        }

        return transactionResult.getTransactionError();
    }

    protected static String getGatewayErrorCode(AuthorizeNetTransactionsRecord transactionResult) {
        if (MysqlAdapter.isTransactionSuccessful(transactionResult)) {
            return "";
        }

        return transactionResult.getTransactionStatus();
    }

    protected static PaymentPluginStatus getPaymentPluginStatus(AuthorizeNetTransactionsRecord transactionResult) {
        return PaymentPluginStatus.valueOf(transactionResult.getKbPaymentPluginStatus());
    }

}
