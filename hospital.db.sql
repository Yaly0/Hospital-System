BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "treatment" (
	"id"	INTEGER NOT NULL UNIQUE,
	"treatment_name"	TEXT NOT NULL,
	PRIMARY KEY("id" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "patient" (
	"id"	INTEGER NOT NULL UNIQUE,
	"first_name"	TEXT NOT NULL,
	"last_name"	TEXT NOT NULL,
	"home_address"	TEXT NOT NULL,
	"birth_date"	TEXT NOT NULL,
	"citizen_number"	TEXT NOT NULL,
	"phone_number"	TEXT NOT NULL,
	"email_address"	TEXT NOT NULL,
	"gender"	TEXT NOT NULL,
	"blood_type"	TEXT NOT NULL,
	"height"	INTEGER NOT NULL,
	"weight"	REAL NOT NULL,
	PRIMARY KEY("id" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "medical_major" (
	"id"	INTEGER NOT NULL,
	"major_name"	TEXT NOT NULL,
	PRIMARY KEY("id")
);
CREATE TABLE IF NOT EXISTS "disease" (
	"id"	INTEGER,
	"disease_name"	TEXT,
	"medical_major_id"	INTEGER,
	FOREIGN KEY("medical_major_id") REFERENCES "medical_major"("id"),
	PRIMARY KEY("id" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "disease_treatment" (
	"disease_id"	INTEGER,
	"treatment_id"	INTEGER,
	FOREIGN KEY("treatment_id") REFERENCES "treatment"("id"),
	FOREIGN KEY("disease_id") REFERENCES "disease"("id"),
	PRIMARY KEY("disease_id","treatment_id")
);
CREATE TABLE IF NOT EXISTS "doctor" (
	"id"	INTEGER NOT NULL UNIQUE,
	"first_name"	TEXT NOT NULL,
	"last_name"	TEXT NOT NULL,
	"home_address"	TEXT NOT NULL,
	"birth_date"	TEXT NOT NULL,
	"citizen_number"	TEXT NOT NULL,
	"phone_number"	TEXT NOT NULL,
	"email_address"	TEXT NOT NULL,
	"gender"	TEXT NOT NULL,
	"blood_type"	TEXT NOT NULL,
	"medical_major_id"	INTEGER NOT NULL,
	"shift_hours"	TEXT NOT NULL,
	"number_of_visits"	INTEGER NOT NULL,
	"sum_of_ratings"	INTEGER NOT NULL,
	FOREIGN KEY("medical_major_id") REFERENCES "medical_major"("id"),
	PRIMARY KEY("id" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "appointment" (
	"id"	INTEGER NOT NULL UNIQUE,
	"patient_id"	INTEGER,
	"doctor_id"	INTEGER,
	"disease_id"	INTEGER,
	"appointment_date"	TEXT,
	"appointment_time"	TEXT,
	"treatment_description"	TEXT,
	"appointment_report"	TEXT,
	"previous_appointment_id"	INTEGER,
	"next_appointment_id"	INTEGER,
	FOREIGN KEY("patient_id") REFERENCES "patient"("id"),
	FOREIGN KEY("disease_id") REFERENCES "disease"("id"),
	FOREIGN KEY("doctor_id") REFERENCES "doctor"("id"),
	FOREIGN KEY("previous_appointment_id") REFERENCES "appointment"("id"),
	FOREIGN KEY("next_appointment_id") REFERENCES "appointment"("id"),
	PRIMARY KEY("id" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "appointment_treatment" (
	"appointment_id"	INTEGER,
	"treatment_id"	INTEGER,
	"disease_id"	INTEGER,
	"treatment_disease_rating"	INTEGER,
	FOREIGN KEY("appointment_id") REFERENCES "appointment"("id"),
	FOREIGN KEY("treatment_id") REFERENCES "treatment"("id"),
	FOREIGN KEY("disease_id") REFERENCES "disease"("id"),
	PRIMARY KEY("appointment_id","treatment_id")
);
INSERT INTO "treatment" VALUES (1,'Paracetamol');
INSERT INTO "treatment" VALUES (2,'Dexomen');
INSERT INTO "treatment" VALUES (3,'Fastum gel');
INSERT INTO "treatment" VALUES (4,'Clear eyes');
INSERT INTO "treatment" VALUES (5,'Thera tears');
INSERT INTO "treatment" VALUES (6,'Cardioace');
INSERT INTO "treatment" VALUES (7,'Ultimate memory');
INSERT INTO "treatment" VALUES (8,'Dent tabs');
INSERT INTO "treatment" VALUES (9,'Stimuloton');
INSERT INTO "patient" VALUES (1,'Tarik','Memić','Trg djece Dobrinje 19, Sarajevo 71000','1995-09-21','2109995180122','+387 62 347 894','mema95@gmail.com','Male','O+',189,92.3);
INSERT INTO "patient" VALUES (2,'Ema','Hamzić','Dzemala Bijedica St 178, Sarajevo 71000','1989-11-18','1811989175064','+387 61 187 829','emaham22@gmail.com','Female','A+',173,62.4);
INSERT INTO "patient" VALUES (3,'Ajna','Džafić','Šenoina 1, Sarajevo 71000','2007-02-04','0402007196210','+387 61 034 511','jasmindzafic@gmail.com','Female','AB-',152,45.5);
INSERT INTO "patient" VALUES (4,'Amar','Redžić','Džidžikovac 3, Sarajevo 71000','1963-05-06','0605963181002','+387 60 377 9355','amarrr63@hotmail.com','Male','B+',178,95.8);
INSERT INTO "medical_major" VALUES (1,'General');
INSERT INTO "medical_major" VALUES (2,'Cardiology');
INSERT INTO "medical_major" VALUES (3,'Stomatology');
INSERT INTO "medical_major" VALUES (4,'Orthopedics');
INSERT INTO "medical_major" VALUES (5,'Optometry');
INSERT INTO "medical_major" VALUES (6,'Psychiatry');
INSERT INTO "disease" VALUES (1,'Headache',1);
INSERT INTO "disease" VALUES (2,'Stomachache',1);
INSERT INTO "disease" VALUES (3,'Heart attack',2);
INSERT INTO "disease" VALUES (4,'Stroke',2);
INSERT INTO "disease" VALUES (5,'Heart failure',2);
INSERT INTO "disease" VALUES (6,'Gum disease',3);
INSERT INTO "disease" VALUES (7,'Toothache',3);
INSERT INTO "disease" VALUES (8,'Oral cancer',3);
INSERT INTO "disease" VALUES (9,'Tooth decay',3);
INSERT INTO "disease" VALUES (10,'Low back pain',4);
INSERT INTO "disease" VALUES (11,'Shoulder pain',4);
INSERT INTO "disease" VALUES (12,'Knee pain',4);
INSERT INTO "disease" VALUES (13,'Cataract',5);
INSERT INTO "disease" VALUES (14,'Diabetic Retinopathy',5);
INSERT INTO "disease" VALUES (15,'Anxiety disorder',6);
INSERT INTO "disease" VALUES (16,'Phobia',6);
INSERT INTO "disease_treatment" VALUES (3,7);
INSERT INTO "disease_treatment" VALUES (4,7);
INSERT INTO "disease_treatment" VALUES (15,7);
INSERT INTO "disease_treatment" VALUES (1,1);
INSERT INTO "disease_treatment" VALUES (1,2);
INSERT INTO "disease_treatment" VALUES (2,1);
INSERT INTO "disease_treatment" VALUES (10,3);
INSERT INTO "disease_treatment" VALUES (11,3);
INSERT INTO "disease_treatment" VALUES (12,3);
INSERT INTO "disease_treatment" VALUES (13,4);
INSERT INTO "disease_treatment" VALUES (14,4);
INSERT INTO "disease_treatment" VALUES (13,5);
INSERT INTO "disease_treatment" VALUES (14,5);
INSERT INTO "disease_treatment" VALUES (8,5);
INSERT INTO "disease_treatment" VALUES (3,6);
INSERT INTO "disease_treatment" VALUES (4,6);
INSERT INTO "disease_treatment" VALUES (5,6);
INSERT INTO "disease_treatment" VALUES (6,8);
INSERT INTO "disease_treatment" VALUES (7,8);
INSERT INTO "disease_treatment" VALUES (8,8);
INSERT INTO "disease_treatment" VALUES (9,8);
INSERT INTO "disease_treatment" VALUES (16,9);
INSERT INTO "disease_treatment" VALUES (7,2);
INSERT INTO "disease_treatment" VALUES (9,2);
INSERT INTO "disease_treatment" VALUES (11,2);
INSERT INTO "doctor" VALUES (1,'Oliver','Hadžiefendić','92 Wrangler Street Ridgewood, NY 11385','1974-05-15','1505974172341','+387 62 533 657','oliver74@gmail.com','Male','A+',1,'08:00-16:00 / {12:00-12:30}',0,0);
INSERT INTO "doctor" VALUES (2,'Jack','Lalić','33 Beacon Dr. Brentwood, NY 11717','1980-04-25','2504980180012','+387 62 938 940','lalic80@hotmail.com','Male','B+',2,'08:00-16:00 / {12:00-12:30}',0,0);
INSERT INTO "doctor" VALUES (3,'Emily','Džananović','9164 S. Birchwood St. Staten Island, NY 10312','1985-02-03','0302985176542','+387 61 456 287','emilydz@yahoo.com','Female','A+',3,'12:00-20:00 / {16:00-16:30}',0,0);
INSERT INTO "doctor" VALUES (4,'Olivia','Husanović','254 Walnut St. Brooklyn, NY 11218','1983-01-12','1201983195525','+387 60 213 9500','husanovico@gmail.com','Female','AB+',4,'08:00-16:00',0,0);
INSERT INTO "doctor" VALUES (5,'Muhammad','Samardžija','33 Beacon Dr. Brentwood, NY 11717','1990-11-01','0111990181226','+387 62 546 768','Samardzijam33@gmail.com','Male','O+',5,'08:00-16:00 / {12:00-12:30}',0,0);
INSERT INTO "doctor" VALUES (6,'Charlie','Hadži','7053 Carriage Street Rochester, NY 14609','1988-08-29','2908988170007','+387 63 555 090','charlie88@hotmail.com','Male','O-',6,'08:00-16:00 / {12:00-12:15}',0,0);
INSERT INTO "doctor" VALUES (7,'Lisa','Kajić','23 Riverside Ave. Jamaica, NY 11432','1979-07-30','3007979177474','+387 66 763 683','lisalisa79@icloud.com','Female','B-',4,'08:00-16:00 / {12:00-12:30}',0,0);
INSERT INTO "appointment" VALUES (1,2,3,7,'2020-10-12','14:30',NULL,NULL,NULL,NULL);
INSERT INTO "appointment" VALUES (2,4,5,14,'2020-10-19','09:45',NULL,NULL,NULL,NULL);
INSERT INTO "appointment" VALUES (3,3,3,7,'2020-11-02','11:00',NULL,NULL,NULL,NULL);
INSERT INTO "appointment_treatment" VALUES (1,2,7,4);
INSERT INTO "appointment_treatment" VALUES (1,8,7,5);
INSERT INTO "appointment_treatment" VALUES (2,4,14,4);
INSERT INTO "appointment_treatment" VALUES (2,5,14,5);
INSERT INTO "appointment_treatment" VALUES (3,2,7,5);
COMMIT;
