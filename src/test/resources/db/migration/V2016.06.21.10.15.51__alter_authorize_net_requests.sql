ALTER TABLE authorize_net_requests ADD kb_ref_transaction_record_id BIGINT(20) UNSIGNED NULL; -- referenced authorize_net_transactions record_id, used in refund transactions