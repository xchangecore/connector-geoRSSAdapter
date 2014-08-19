package com.saic.uicds.clients.em.georssadapter.data;

import org.apache.xmlbeans.XmlObject;

import x0.messageStructure1.PackageMetadataType;
import x0.messageStructure1.StructuredPayloadType;

import com.saic.precis.x2009.x06.base.IdentificationType;
import com.saic.precis.x2009.x06.structures.WorkProductDocument;
import com.saic.precis.x2009.x06.structures.WorkProductDocument.WorkProduct;
import com.saic.uicds.clients.util.Common;

public class Util {

    public static WorkProduct toWorkProductDocument(String type, String version,
        IdentificationType identification, XmlObject content) {

        WorkProduct workProduct = WorkProductDocument.Factory.newInstance().addNewWorkProduct();
        PackageMetadataType pkgMetadata = workProduct.addNewPackageMetadata();
        XmlObject metadata = pkgMetadata.addNewPackageMetadataExtensionAbstract();
        Common.setIdentifierElement(metadata, identification);

        StructuredPayloadType payload = workProduct.addNewStructuredPayload();
        payload.set(content);
        payload.addNewStructuredPayloadMetadata().setCommunityURI(type);
        payload.getStructuredPayloadMetadata().setCommunityVersion(version);

        return workProduct;
    }
}
