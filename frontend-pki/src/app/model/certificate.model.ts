import { CertificateData } from "./certificateData.model";

export class Certificate{
    id: number = -1;
    commonName: string = "undefined";
    validFrom?: Date | string = "02/03/2022";
    validTo?: Date | string = "02/03/2022";
    email : string = "undefined";
    certificateType : string = "";
    certificateData : CertificateData;
    isValid : boolean;
    //user: User;

    // constructor(commonName: string, revoked: boolean, validFrom: Date, validTo: Date){
    //     this.commonName = commonName;
    //     this.revoked = revoked;
    //     this.validFrom = validFrom;
    //     this.validTo = validTo;
    // }
}
