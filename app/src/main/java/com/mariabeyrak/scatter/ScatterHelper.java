package com.mariabeyrak.scatter;

import com.mariabeyrak.scatter.models.requests.transaction.request.Action;
import com.mariabeyrak.scatter.models.requests.transaction.request.Authorization;
import com.mariabeyrak.scatter.models.requests.transaction.request.Transaction;
import com.mariabeyrak.scatter.models.requests.transaction.request.TransactionRequestParams;
import com.paytomat.core.util.ByteSerializer;
import com.paytomat.eos.Eos;
import com.paytomat.eos.PrivateKey;
import com.paytomat.eos.signature.Signature;
import com.paytomat.eos.transaction.EosAction;
import com.paytomat.eos.transaction.EosActionAuthorization;
import com.paytomat.eos.transaction.EosActionAuthorizationPermission;
import com.paytomat.eos.transaction.EosExtentionType;
import com.paytomat.eos.transaction.EosTransaction;

import org.bouncycastle.util.encoders.Hex;

import java.util.Arrays;

public class ScatterHelper {

    public static String[] getSignaturesForSerializedTransaction(String transaction,
                                                                 String chainId,
                                                                 PrivateKey privateKey) {
        byte[] serialized = Hex.decode(transaction);
        byte[] trail = new byte[32];
        Arrays.fill(trail, (byte) 0);
        byte[] signData = new ByteSerializer()
                .writeHex(chainId)
                .write(serialized)
                .write(trail)
                .serialize();

        Signature signature = Eos.signTransactionRaw(signData, privateKey);
        return new String[]{signature.toString()};
    }

    public static EosTransaction toEosTransaction(TransactionRequestParams params, PrivateKey privateKey) {
        return new EosTransaction(
                privateKey, getChainIdHex(params), params.getEstimatedInSeconds(),
                (short) params.getTransaction().getRefBlockNum(), (int) params.getTransaction().getRefBlockPrefix(),
                params.getTransaction().getMaxNetUsageWords(), (byte) params.getTransaction().getMaxCpuUsageMs(),
                params.getTransaction().getDelaySec(), new EosAction[]{}, getEosActions(params.getTransaction()), new EosExtentionType[]{}
        );
    }

    private static String getChainIdHex(TransactionRequestParams params) {
        byte[] byteArray = new byte[32];
        System.arraycopy(params.getBuf().getData(), 0, byteArray, 0, 32);
        return Hex.toHexString(byteArray);
    }

    private static EosAction[] getEosActions(Transaction transaction) {
        EosAction[] array = new EosAction[transaction.getActions().length];
        for (int i = 0; i < transaction.getActions().length; i++) {
            array[i] = toEosAction(transaction.getActions()[i]);
        }
        return array;
    }

    private static EosAction toEosAction(Action action) {
        return new EosAction(action.getAccount(), action.getName(), getEosActionAuthorization(action), Hex.decode(action.getData()));
    }

    private static EosActionAuthorization[] getEosActionAuthorization(Action action) {
        EosActionAuthorization[] array = new EosActionAuthorization[action.getAuthorization().length];
        for (int i = 0; i < action.getAuthorization().length; i++) {
            array[i] = toEosActionAuthorization(action.getAuthorization()[i]);
        }
        return array;
    }

    private static EosActionAuthorization toEosActionAuthorization(Authorization authorization) {
        return new EosActionAuthorization(authorization.getActor(), getEosActionAuthorizationPermissionByValue(authorization.getPermission()));
    }

    private static EosActionAuthorizationPermission getEosActionAuthorizationPermissionByValue(String value) {
        for (EosActionAuthorizationPermission e : EosActionAuthorizationPermission.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return EosActionAuthorizationPermission.ACTIVE;
    }
}

