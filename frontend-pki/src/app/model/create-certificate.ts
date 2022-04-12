import { CertificateData } from "./certificateData.model";

export class CreateCertificate {
    validFrom?: Date | string;
    validTo?: Date | string;
    issuerName: string;     //email
    subjectName: string;    //email
    certificateDataDTO : CertificateData = new CertificateData();
    certificateType : string;	
    issuerSerialNumber: string;
    certificateUsage: string;
}
