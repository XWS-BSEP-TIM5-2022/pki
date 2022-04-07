export class Certificate{
    id: number = -1;
    commonName: string = "Menjaj"; 
    validFrom?: Date | string = "02/03/2022";
    validTo?: Date | string = "02/03/2022";
    //user: User;

    // constructor(commonName: string, revoked: boolean, validFrom: Date, validTo: Date){
    //     this.commonName = commonName;
    //     this.revoked = revoked;
    //     this.validFrom = validFrom;
    //     this.validTo = validTo;
    // }
}