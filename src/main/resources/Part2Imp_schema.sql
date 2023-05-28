DROP DATABASE IF EXISTS HospitalDB;

CREATE DATABASE HospitalDB;

USE HospitalDB;

CREATE TABLE Doctor (
  ID INT AUTO_INCREMENT PRIMARY KEY,
  last_name VARCHAR(50) NOT NULL,
  first_name VARCHAR(50) NOT NULL,
  practice_since INT NOT NULL, 
  specialty VARCHAR(50) NOT NULL,
  ssn VARCHAR(20) UNIQUE,
  INDEX (ssn)
);

CREATE TABLE Patient (
  patientId INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  last_name VARCHAR(255) DEFAULT NULL,
  first_name VARCHAR(255) DEFAULT NULL,
  birthdate DATE DEFAULT NULL,
  ssn VARCHAR(20) DEFAULT NULL UNIQUE,
  street VARCHAR(255) DEFAULT NULL,
  city VARCHAR(255) DEFAULT NULL,
  state VARCHAR(255) DEFAULT NULL,
  zipcode VARCHAR(10) DEFAULT NULL,
  primaryName VARCHAR(255) default null,
  INDEX (ssn)
);

CREATE TABLE PharmaceuticalCompany (
  Name VARCHAR(50) PRIMARY KEY,
  PhoneNumber VARCHAR(20) NOT NULL
);

--
-- Table structure for table `drug`
--
CREATE TABLE drug (
  drug_id int(11) NOT NULL,
  trade_name varchar(100) DEFAULT NULL,
  formula varchar(200) DEFAULT NULL,
  PRIMARY KEY (drug_id)
); 

INSERT INTO drug VALUES 
(1,'Tylenol with Codeine','acetaminophen and codeine'),
(2,'Proair Proventil','albuterol aerosol'),
(3,'Accuneb','albuterol HFA'),
(4,'Fosamax','alendronate'),
(5,'Zyloprim','allopurinol'),
(6,'Xanax','alprazolam'),
(7,'Elavil','amitriptyline'),
(8,'Augmentin','amoxicillin and clavulanate K+'),
(9,'Amoxil','amoxicillin'),
(10,'Adderall XR','amphetamine and dextroamphetamine XR'),
(11,'Tenormin','atenolol'),
(12,'Lipitor','atorvastatin'),
(13,'Zithromax','azithromycin'),
(14,'Lotrel','benazepril and amlodipine'),
(15,'Soma','carisoprodol'),
(16,'Coreg','carvedilol'),
(17,'Omnicef','cefdinir'),
(18,'Celebrex','celecoxib'),
(19,'Keflex','cephalexin'),
(20,'Cipro','ciprofloxacin'),
(21,'Celexa','citalopram'),
(22,'Klonopin','clonazepam'),
(23,'Catapres','clonidine HCl'),
(24,'Plavix','clopidogrel'),
(25,'Premarin','conjugated estrogens'),
(26,'Flexeril','cyclobenzaprine'),
(27,'Valium','diazepam'),
(28,'Voltaren','diclofenac sodium'),
(29,'Yaz','drospirenone and ethinyl estradiol'),
(30,'Cymbalta','Duloxetine'),
(31,'Vibramycin','doxycycline hyclate'),
(32,'Vasotec','enalapril'),
(33,'Lexapro','escitalopram'),
(34,'Nexium','esomeprazole'),
(35,'Zetia','ezetimibe'),
(36,'Tricor','fenofibrate'),
(37,'Allegra','fexofenadine'),
(38,'Diflucan','fluconozole'),
(39,'Prozac','fluoxetine HCl'),
(40,'Advair','fluticasone and salmeterol inhaler'),
(41,'Flonase','fluticasone nasal spray'),
(42,'Folic Acid','folic acid'),
(43,'Lasix','furosemide'),
(44,'Neurontin','gabapentin'),
(45,'Amaryl','glimepiride'),
(46,'Diabeta','glyburide'),
(47,'Glucotrol','glipizide'),
(48,'Microzide','hydrochlorothiazide'),
(49,'Lortab','hydrocodone and acetaminophen'),
(50,'Motrin','ibuprophen'),
(51,'Lantus','insulin glargine'),
(52,'Imdur','isosorbide mononitrate'),
(53,'Prevacid','lansoprazole'),
(54,'Levaquin','levofloxacin'),
(55,'Levoxl','levothyroxine sodium'),
(56,'Zestoretic','lisinopril and hydrochlorothiazide'),
(57,'Prinivil','lisinopril'),
(58,'Ativan','lorazepam'),
(59,'Cozaar','losartan'),
(60,'Mevacor','lovastatin'),
(61,'Mobic','meloxicam'),
(62,'Glucophage','metformin HCl'),
(63,'Medrol','methylprednisone'),
(64,'Toprol','metoprolol succinate XL'),
(65,'Lopressor','metoprolol tartrate'),
(66,'Nasonex','mometasone'),
(67,'Singulair','montelukast'),
(68,'Naprosyn','naproxen'),
(69,'Prilosec','omeprazole'),
(70,'Percocet','oxycodone and acetaminophen'),
(71,'Protonix','pantoprazole'),
(72,'Paxil','paroxetine'),
(73,'Actos','pioglitazone'),
(74,'Klor-Con','potassium Chloride'),
(75,'Pravachol','pravastatin'),
(76,'Deltasone','prednisone'),
(77,'Lyrica','pregabalin'),
(78,'Phenergan','promethazine'),
(79,'Seroquel','quetiapine'),
(80,'Zantac','ranitidine'),
(81,'Crestor','rosuvastatin'),
(82,'Zoloft','sertraline HCl'),
(83,'Viagra','sildenafil HCl'),
(84,'Vytorin','simvastatin and ezetimibe'),
(85,'Zocor','simvastatin'),
(86,'Aldactone','spironolactone'),
(87,'Bactrim DS','sulfamethoxazole and trimethoprim DS'),
(88,'Flomax','tamsulosin'),
(89,'Restoril','temezepam'),
(90,'Topamax','topiramate'),
(91,'Ultram','tramadol'),
(92,'Aristocort','triamcinolone Ace topical'),
(93,'Desyrel','trazodone HCl'),
(94,'Dyazide','triamterene and hydrochlorothiazide'),
(95,'Valtrex','valaciclovir'),
(96,'Diovan','valsartan'),
(97,'Effexor XR','venlafaxine XR'),
(98,'Calan SR','verapamil SR'),
(99,'Ambien','zolpidem');

CREATE TABLE Pharmacy (
  PharmacyId INT(8) PRIMARY KEY AUTO_INCREMENT,
  Name VARCHAR(50),
  Address VARCHAR(50) NOT NULL,
  PhoneNumber VARCHAR(20) NOT NULL,
  INDEX (Name)
);

CREATE TABLE PharmacyDrugs (
  PharmacyName VARCHAR(50),
  DrugId INT(11),
  Price DECIMAL(10, 2) NOT NULL CHECK (Price >= 0),
  PRIMARY KEY (PharmacyName, DrugId),
  FOREIGN KEY (PharmacyName) REFERENCES Pharmacy(Name),
  FOREIGN KEY (DrugId) REFERENCES Drug(drug_id)  
);

CREATE TABLE Prescription (
  RXNumber INT PRIMARY KEY AUTO_INCREMENT,
  DrugName VARCHAR(255) NOT NULL,
  Quantity INT NOT NULL CHECK (Quantity > 0),
  Patient_SSN VARCHAR(20) NOT NULL,
  PatientFirstName VARCHAR(255) NOT NULL,
  PatientLastName VARCHAR(255) NOT NULL,
  Doctor_SSN VARCHAR(20) NOT NULL,
  DoctorFirstName VARCHAR(255) NOT NULL,
  DoctorLastName VARCHAR(255) NOT NULL,
  PharmacyId INT(8) DEFAULT NULL,
  DateFilled DATE DEFAULT NULL,
  FOREIGN KEY (Patient_SSN) REFERENCES Patient(ssn),
  FOREIGN KEY (Doctor_SSN) REFERENCES Doctor(ssn),
  FOREIGN KEY (PharmacyId) REFERENCES Pharmacy(pharmacyid)
);

CREATE TABLE Contract (
  ContractID INT PRIMARY KEY,
  CompanyName VARCHAR(50) NOT NULL,
  PharmacyName VARCHAR(50) NOT NULL,
  StartDate DATE NOT NULL,
  EndDate DATE NOT NULL,
  ContractText TEXT NOT NULL,
  Supervisor VARCHAR(50) NOT NULL,
  FOREIGN KEY (CompanyName) REFERENCES PharmaceuticalCompany(Name),
  FOREIGN KEY (PharmacyName) REFERENCES Pharmacy(Name)
);

CREATE TABLE PrescriptionFilling (
  RXNumber INT,
  PharmacyName VARCHAR(50) NOT NULL,
  FilledDate DATE NOT NULL,
  SupplierCompanyName VARCHAR(50),
  PRIMARY KEY (RXNumber, PharmacyName),
  FOREIGN KEY (RXNumber) REFERENCES Prescription(RXNumber),
  FOREIGN KEY (PharmacyName) REFERENCES Pharmacy(Name),
  FOREIGN KEY (SupplierCompanyName) REFERENCES PharmaceuticalCompany(Name)
);
INSERT INTO Pharmacy (Name, Address, PhoneNumber) VALUES ('ABC Pharmacy', '123 Main Street', '555-1234');

SELECT * FROM doctor;
SELECT * FROM patient;
select * from prescription;
select * from pharmacy;
