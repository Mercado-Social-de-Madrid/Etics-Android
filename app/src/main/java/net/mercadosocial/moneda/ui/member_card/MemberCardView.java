package net.mercadosocial.moneda.ui.member_card;

import android.graphics.Bitmap;

import net.mercadosocial.moneda.base.BaseView;
import net.mercadosocial.moneda.model.Account;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.model.Person;

public interface MemberCardView extends BaseView {
    void showMemberData(Account account, String memberType);

    void showQrBitmap(Bitmap qrBitmap);

    void showQrScanButton(boolean show);

    void showLogoutView(boolean showLogoutView);

    void showInactiveMemberView(boolean show);
}
