package org.naukma.zlagoda.sale;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.naukma.zlagoda.check.CheckEntity;
import org.naukma.zlagoda.storeproduct.StoreProductEntity;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleId {
    private StoreProductEntity storeProduct;
    private CheckEntity checkEntity;
}
