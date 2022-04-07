export class Certificate{
    commonName: string = ""; 
    revoked: boolean = false;
	validFrom: Date = new Date();
    validTo: Date = new Date();
    //user: User;

    // constructor(commonName: string, revoked: boolean, validFrom: Date, validTo: Date){
    //     this.commonName = commonName;
    //     this.revoked = revoked;
    //     this.validFrom = validFrom;
    //     this.validTo = validTo;
    // }
}