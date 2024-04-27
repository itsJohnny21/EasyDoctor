package edu.asu.easydoctor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import edu.asu.easydoctor.Database.Role;

public abstract  class DataRow {
    public String tableName;
    public Integer rowID;

    public static class User extends DataRow {
        public Datum userID;
        public Datum role;
        public Datum username;

        public static User getFor(int userID) throws SQLException {
            ResultSet resultSet = Database.selectRow(userID, "users");

            if (!resultSet.next()) {
                return null;
            }

            User user = new User(resultSet);
            resultSet.close();
            
            return user;
        }

        public User(ResultSet resultSet) throws SQLException {
            this.tableName = "users";

            String rowIDColumn = "ID";
            String userIDColumn = "ID";
            String roleColumn = "role";
            String usernameColumn = "username";

            this.rowID = resultSet.getInt(rowIDColumn);
            this.userID = new Datum(this, resultSet.getString(userIDColumn), userIDColumn);
            this.role = new Datum(this, resultSet.getString(roleColumn), roleColumn);
            this.username = new Datum(this, resultSet.getString(usernameColumn), usernameColumn);
        }
    }

    public static class Allergy extends DataRow {
        public Datum userID;
        public Datum allergen;
        public Datum commonSource;
        public Datum severity;
        public Datum type;
        public Datum notes;

        public static ArrayList<Allergy> getAllFor(int userID) throws SQLException {
            ResultSet resultSet = Database.selectMultiRow(userID, "allergies");
            ArrayList<Allergy> allergies = new ArrayList<Allergy>();
            
            while (resultSet.next()) {
                allergies.add(new Allergy(resultSet));
            }
            resultSet.close();

            if (allergies.size() == 0) {
                return null;
            }

            return allergies;
        }

        public Allergy(ResultSet resultSet) throws SQLException {
            this.tableName = "allergies";

            String userIDColumn = "userID";
            String allergenColumn = "allergen";
            String commonSourceColumn = "commonSource";
            String severityColumn = "severity";
            String typeColumn = "type";
            String notesColumn = "notes";


            this.rowID = resultSet.getInt("ID");
            this.userID = new Datum(this, resultSet.getString(userIDColumn), userIDColumn);
            this.allergen = new Datum(this, resultSet.getString(allergenColumn), allergenColumn);
            this.commonSource = new Datum(this, resultSet.getString(commonSourceColumn), commonSourceColumn);
            this.severity = new Datum(this, resultSet.getString(severityColumn), severityColumn);
            this.type = new Datum(this, resultSet.getString(typeColumn), typeColumn);
            this.notes = new Datum(this, resultSet.getString(notesColumn), notesColumn);
        }
    }

    public static class Surgery extends DataRow {
        public Datum userID;
        public Datum doctorID;
        public Datum date;
        public Datum type;
        public Datum location;
        public Datum notes;

        public static ArrayList<Surgery> getAllFor(int userID) throws SQLException {
            ResultSet resultSet = Database.selectMultiRow(userID, "surgeries");
            ArrayList<Surgery> surgeries = new ArrayList<Surgery>();
            
            while (resultSet.next()) {
                surgeries.add(new Surgery(resultSet));
            }
            resultSet.close();

            return surgeries;
        }

        public Surgery(ResultSet resultSet) throws SQLException {
            this.tableName = "surgeries";

            String userIDColumn = "userID";
            String doctorIDColumn = "doctorID";
            String typeColumn = "type";
            String dateColumn = "date";
            String locationColumn = "location";
            String notesColumn = "notes";

            this.rowID = resultSet.getInt("ID");
            this.userID = new Datum(this, resultSet.getString(userIDColumn), userIDColumn);
            this.doctorID = new Datum(this, resultSet.getString(doctorIDColumn), doctorIDColumn);
            this.type = new Datum(this, resultSet.getString(typeColumn), typeColumn);
            this.date = new Datum(this, resultSet.getString(dateColumn), dateColumn);
            this.location = new Datum(this, resultSet.getString(locationColumn), locationColumn);
            this.notes = new Datum(this, resultSet.getString(notesColumn), notesColumn);
        }
    }

    public static class Patient extends DataRow {
        public Datum username;
        public Datum firstName;
        public Datum lastName;
        public Datum sex;
        public Datum birthDate;
        public Datum email;
        public Datum phone;
        public Datum address;
        public Datum preferredDoctorID;
        public Datum bloodType;
        public Datum height;
        public Datum weight;
        public Datum race;
        public Datum ethnicity;
        public Datum insuranceProviderID;
        public Datum pharmacyID;
        public Datum insuranceID;
        public Datum emergencyContactName;
        public Datum emergencyContactPhone;
        public Datum motherFirstName;
        public Datum motherLastName;
        public Datum fatherFirstName;
        public Datum fatherLastName;
        public Datum preferredDoctorFirstName;
        public Datum preferredDoctorLastName;
        public Datum insuranceProviderName;
        public Datum pharmacyName;

        public Patient(ResultSet resultSet) throws SQLException {
            this.tableName = "patients";

            String rowIDColumn = "ID";
            String usernameColumn = "username";
            String firstNameColumn = "firstName";
            String lastNameColumn = "lastName";
            String sexColumn = "sex";
            String birthDateColumn = "birthDate";
            String emailColumn = "email";
            String phoneColumn = "phone";
            String addressColumn = "address";
            String preferredDoctorIDColumn = "preferredDoctorID";
            String bloodTypeColumn = "bloodType";
            String heightColumn = "height";
            String weightColumn = "weight";
            String raceColumn = "race";
            String ethnicityColumn = "ethnicity";
            String insuranceProviderIDColumn = "insuranceProviderID";
            String pharmacyIDColumn = "pharmacyID";
            String insuranceIDColumn = "insuranceID";
            String emergencyContactNameColumn = "emergencyContactName";
            String emergencyContactPhoneColumn = "emergencyContactPhone";
            String motherFirstNameColumn = "motherFirstName";
            String motherLastNameColumn = "motherLastName";
            String fatherFirstNameColumn = "fatherFirstName";
            String fatherLastNameColumn = "fatherLastName";
            String preferredDoctorFirstNameColumn = "preferredDoctorFirstName";
            String preferredDoctorLastNameColumn = "preferredDoctorLastName";
            String pharmacyNameColumn = "pharmacyName";
            String insuranceProviderNameColumn = "insuranceProviderName";

            this.rowID = resultSet.getInt(rowIDColumn);
            this.username = new Datum(this, resultSet.getString(usernameColumn), usernameColumn);
            this.firstName = new Datum(this, resultSet.getString(firstNameColumn), firstNameColumn);
            this.lastName = new Datum(this, resultSet.getString(lastNameColumn), lastNameColumn);
            this.sex = new Datum(this, resultSet.getString(sexColumn), sexColumn);
            this.birthDate = new Datum(this, resultSet.getString(birthDateColumn), birthDateColumn);
            this.email = new Datum(this, resultSet.getString(emailColumn), emailColumn);
            this.phone = new Datum(this, resultSet.getString(phoneColumn), phoneColumn);
            this.address = new Datum(this, resultSet.getString(addressColumn), addressColumn);
            this.preferredDoctorID = new Datum(this, resultSet.getString(preferredDoctorIDColumn), preferredDoctorIDColumn);
            this.bloodType = new Datum(this, resultSet.getString(bloodTypeColumn), bloodTypeColumn);
            this.height = new Datum(this, resultSet.getString(heightColumn), heightColumn);
            this.weight = new Datum(this, resultSet.getString(weightColumn), weightColumn);
            this.race = new Datum(this, resultSet.getString(raceColumn), raceColumn);
            this.ethnicity = new Datum(this, resultSet.getString(ethnicityColumn), ethnicityColumn);
            this.insuranceProviderID = new Datum(this, resultSet.getString(insuranceProviderIDColumn), insuranceProviderIDColumn);
            this.pharmacyID = new Datum(this, resultSet.getString(pharmacyIDColumn), pharmacyIDColumn);
            this.insuranceID = new Datum(this, resultSet.getString(insuranceIDColumn), insuranceIDColumn);
            this.emergencyContactName = new Datum(this, resultSet.getString(emergencyContactNameColumn), emergencyContactNameColumn);
            this.emergencyContactPhone = new Datum(this, resultSet.getString(emergencyContactPhoneColumn), emergencyContactPhoneColumn);
            this.motherFirstName = new Datum(this, resultSet.getString(motherFirstNameColumn), motherFirstNameColumn);
            this.motherLastName = new Datum(this, resultSet.getString(motherLastNameColumn), motherLastNameColumn);
            this.fatherFirstName = new Datum(this, resultSet.getString(fatherFirstNameColumn), fatherFirstNameColumn);
            this.fatherLastName = new Datum(this, resultSet.getString(fatherLastNameColumn), fatherLastNameColumn);
            this.preferredDoctorFirstName = new Datum(this, resultSet.getString(preferredDoctorFirstNameColumn), preferredDoctorFirstNameColumn);
            this.preferredDoctorLastName = new Datum(this, resultSet.getString(preferredDoctorLastNameColumn), preferredDoctorLastNameColumn);
            this.pharmacyName = new Datum(this, resultSet.getString(pharmacyNameColumn), pharmacyNameColumn);
            this.insuranceProviderName = new Datum(this, resultSet.getString(insuranceProviderNameColumn), insuranceProviderNameColumn);
        }

        public static Patient getFor(int userID) throws SQLException {
            ResultSet resultSet = Database.getPatient(userID);

            if (!resultSet.next()) {
                return null;
            }

            Patient patient = new Patient(resultSet);
            resultSet.close();

            return patient;
        }

        public Datum getPreferredDoctor() throws SQLException {
            Employee employee = Employee.getFor(Integer.parseInt(preferredDoctorID.originalValue));
            preferredDoctorID.displayValue = employee.firstName.originalValue + " " + employee.lastName.originalValue;
            return preferredDoctorID;
        }
    }

    public static class Employee extends DataRow {
        public Datum userID;
        public Datum firstName;
        public Datum lastName;
        public Datum sex;
        public Datum birthDate;
        public Datum email;
        public Datum phone;
        public Datum address;
        public Datum managerID;

        public Employee(ResultSet resultSet) throws SQLException {
            this.tableName = "employees";

            String rowIDColumn = "ID";
            String userIDColumn = "ID";
            String firstNameColumn = "firstName";
            String lastNameColumn = "lastName";
            String sexColumn = "sex";
            String birthDateColumn = "birthDate";
            String emailColumn = "email";
            String phoneColumn = "phone";
            String addressColumn = "address";
            String managerIDColumn = "managerID";

            this.rowID = resultSet.getInt(rowIDColumn);
            this.userID = new Datum(this, resultSet.getString(userIDColumn), userIDColumn);
            this.firstName = new Datum(this, resultSet.getString(firstNameColumn), firstNameColumn);
            this.lastName = new Datum(this, resultSet.getString(lastNameColumn), lastNameColumn);
            this.sex = new Datum(this, resultSet.getString(sexColumn), sexColumn);
            this.birthDate = new Datum(this, resultSet.getString(birthDateColumn), birthDateColumn);
            this.email = new Datum(this, resultSet.getString(emailColumn), emailColumn);
            this.phone = new Datum(this, resultSet.getString(phoneColumn), phoneColumn);
            this.address = new Datum(this, resultSet.getString(addressColumn), addressColumn);
            this.managerID = new Datum(this, resultSet.getString(managerIDColumn), managerIDColumn);
        }

        public static ArrayList<Employee> getAllDoctors() throws SQLException {
            ArrayList<Employee> doctors = new ArrayList<Employee>();
            ResultSet resultSet = Database.selectAllWithRole(Role.DOCTOR);

            while (resultSet.next()) {
                Employee doctor = Employee.getFor(resultSet.getInt("ID"));
                doctors.add(doctor);
            }
            resultSet.close();

            return doctors;
        }

        public static Employee getFor(int userID) throws SQLException {
            ResultSet resultSet = Database.selectRow(userID, "employees");

            if (!resultSet.next()) {
                return null;
            }

            Employee employee = new Employee(resultSet);
            resultSet.close();

            return employee;
        }

        public static ArrayList<Datum> getAllDoctorNames() throws SQLException {
            ResultSet resultSet = Database.selectAllWithRole(Role.DOCTOR);
            ArrayList<Datum> doctors = new ArrayList<Datum>();

            while (resultSet.next()) {
                User doctor = new User(resultSet);
                doctors.add(doctor.userID);
            }
            resultSet.close();

            return doctors;
        }
    }

    public static class Visit extends DataRow {
        public Datum creationTime;
        public Datum creationType;
        public Datum date;
        public Datum userID;
        public Datum doctorID;
        public Datum completed;
        public Datum reason;
        public Datum description;

        public static ArrayList<Visit> getAllFor(int userID) throws SQLException {
            ResultSet resultSet = Database.selectMultiRow(userID, "visits");
            ArrayList<Visit> visits = new ArrayList<Visit>();
            
            while (resultSet.next()) {
                Visit visit = new Visit(resultSet);
                visits.add(visit);
            }
            resultSet.close();

            return visits;
        }

        public static Visit getFor(int rowID) throws SQLException {
            ResultSet resultSet = Database.selectRow(rowID, "visits");

            if (!resultSet.next()) {
                return null;
            }

            Visit visit = new Visit(resultSet);
            resultSet.close();

            return visit;
        }

        public Visit(ResultSet resultSet) throws SQLException {
            this.tableName = "visits";

            String creationTimeColumn = "creationTime";
            String creationTypeColumn = "creationType";
            String dateColumn = "date";
            String userIDColumn = "userID";
            String doctorIDColumn = "doctorID";
            String completedColumn = "completed";
            String reasonColumn = "reason";
            String descriptionColumn = "description";

            this.rowID = resultSet.getInt("ID");
            this.creationTime = new Datum(this, resultSet.getString(creationTimeColumn), creationTimeColumn);
            this.creationType = new Datum(this, resultSet.getString(creationTypeColumn), creationTypeColumn);
            this.date = new Datum(this, resultSet.getString(dateColumn), dateColumn);
            this.userID = new Datum(this, resultSet.getString(userIDColumn), userIDColumn);
            this.doctorID = new Datum(this, resultSet.getString(doctorIDColumn), doctorIDColumn);
            this.completed = new Datum(this, resultSet.getString(completedColumn), completedColumn);
            this.reason = new Datum(this, resultSet.getString(reasonColumn), reasonColumn);
            this.description = new Datum(this, resultSet.getString(descriptionColumn), descriptionColumn);
        }
    }

    public static class Vaccine extends DataRow {
        public Datum creationTime;
        public Datum userID;
        public Datum vaccineGroup;
        public Datum date;
        public Datum provider;
        public Datum notes;

        public static ArrayList<Vaccine> getAllFor(int userID) throws SQLException {
            ResultSet resultSet = Database.selectMultiRow(userID, "vaccines");
            ArrayList<Vaccine> vaccines = new ArrayList<Vaccine>();
            
            while (resultSet.next()) {
                Vaccine vaccine = new Vaccine(resultSet);
                vaccines.add(vaccine);
            }
            resultSet.close();

            return vaccines;
        }

        public static Vaccine getFor(int rowID) throws SQLException {
            ResultSet resultSet = Database.selectRow(rowID, "vaccines");

            if (!resultSet.next()) {
                return null;
            }

            Vaccine visit = new Vaccine(resultSet);
            resultSet.close();

            return visit;
        }

        public Vaccine(ResultSet resultSet) throws SQLException {
            this.tableName = "vaccines";
            

            String creationTimeColumn = "creationTime";
            String userIDColumn = "userID";
            String vaccineGroupColumn = "vaccineGroup";
            String dateColumn = "date";
            String providerColumn = "provider";
            String notesColumn = "notes";

            this.rowID = resultSet.getInt("ID");
            this.creationTime = new Datum(this, resultSet.getString(creationTimeColumn), creationTimeColumn);
            this.userID = new Datum(this, resultSet.getString(userIDColumn), userIDColumn);
            this.vaccineGroup = new Datum(this, resultSet.getString(vaccineGroupColumn), vaccineGroupColumn);
            this.date = new Datum(this, resultSet.getString(dateColumn), dateColumn);
            this.provider = new Datum(this, resultSet.getString(providerColumn), providerColumn);
            this.notes = new Datum(this, resultSet.getString(notesColumn), notesColumn);
        }
    }

    public static class Prescription extends DataRow {
        public Datum creationTime;
        public Datum userID;
        public Datum doctorID;
        public Datum drugID;
        public Datum intakeDay;
        public Datum intakeTime;
        public Datum dosageQuantity;
        public Datum dosageUnits;

        public static ArrayList<Prescription> getAllFilteredFor(int userID, String filter) throws SQLException {
            ResultSet resultSet = Database.getFilteredPrescriptionsFor(userID, filter);
            ArrayList<Prescription> prescriptions = new ArrayList<Prescription>();
            
            while (resultSet.next()) {
                Prescription prescription = new Prescription(resultSet);
                prescriptions.add(prescription);
            }
            resultSet.close();

            return prescriptions;
        }

        public static ArrayList<Prescription> getAllFor(int userID) throws SQLException {
            ResultSet resultSet = Database.selectMultiRow(userID, "prescriptions");
            ArrayList<Prescription> prescriptions = new ArrayList<Prescription>();
            
            while (resultSet.next()) {
                Prescription prescription = new Prescription(resultSet);
                prescriptions.add(prescription);
            }
            resultSet.close();

            return prescriptions;
        }

        public static Prescription getFor(int rowID) throws SQLException {
            ResultSet resultSet = Database.selectRow(rowID, "prescriptions");

            if (!resultSet.next()) return null;

            Prescription prescription = new Prescription(resultSet);
            resultSet.close();

            return prescription;
        }

        public Prescription(ResultSet resultSet) throws SQLException {
            this.tableName = "prescriptions";

            String creationTimeColumn = "creationTime";
            String userIDColumn = "userID";
            String doctorIDColumn = "doctorID";
            String drugIDColumn = "drugID";
            String intakeDayColumn = "intakeDay";
            String intakeTimeColumn = "intakeTime";
            String dosageQuantityColumn = "dosageQuantity";
            String dosageUnitsColumn = "dosageUnits";

            this.rowID = resultSet.getInt("ID");
            this.creationTime = new Datum(this, resultSet.getString(creationTimeColumn), creationTimeColumn);
            this.userID = new Datum(this, resultSet.getString(userIDColumn), userIDColumn);
            this.doctorID = new Datum(this, resultSet.getString(doctorIDColumn), doctorIDColumn);
            this.drugID = new Datum(this, resultSet.getString(drugIDColumn), drugIDColumn);
            this.intakeDay = new Datum(this, resultSet.getString(intakeDayColumn), intakeDayColumn);
            this.intakeTime = new Datum(this, resultSet.getString(intakeTimeColumn), intakeTimeColumn);
            this.dosageQuantity = new Datum(this, resultSet.getString(dosageQuantityColumn), dosageQuantityColumn);
            this.dosageUnits = new Datum(this, resultSet.getString(dosageUnitsColumn), dosageUnitsColumn);
        }
    }

    public static class Drug extends DataRow {
        public Datum name;
        public Datum manufacturer;
        public Datum shelfLife;
        public Datum instructions;
        public Datum description;
        public Datum sideEffects;
        public Datum storageInformation;
        public Datum activeIngredients;


        public Drug(ResultSet resultSet) throws SQLException {
            this.tableName = "drugs";

            String rowIDColumn = "ID";
            String nameColumn = "name";
            String manufacturerColumn = "manufacturer";
            String shelfLifeColumn = "shelfLife";
            String instructionsColumn = "instructions";
            String descriptionColumn = "description";
            String sideEffectsColumn = "sideEffects";
            String storageInformationColumn = "storageInformation";
            String activeIngredientsColumn = "activeIngredients";

            this.rowID = resultSet.getInt(rowIDColumn);
            this.name = new Datum(this, resultSet.getString(nameColumn), nameColumn);
            this.manufacturer = new Datum(this, resultSet.getString(manufacturerColumn), manufacturerColumn);
            this.shelfLife = new Datum(this, resultSet.getString(shelfLifeColumn), shelfLifeColumn);
            this.instructions = new Datum(this, resultSet.getString(instructionsColumn), instructionsColumn);
            this.description = new Datum(this, resultSet.getString(descriptionColumn), descriptionColumn);
            this.sideEffects = new Datum(this, resultSet.getString(sideEffectsColumn), sideEffectsColumn);
            this.storageInformation = new Datum(this, resultSet.getString(storageInformationColumn), storageInformationColumn);
            this.activeIngredients = new Datum(this, resultSet.getString(activeIngredientsColumn), activeIngredientsColumn);
        }

        public static Drug getFor(int drugID) throws SQLException {
            ResultSet resultSet = Database.selectRow(drugID, "drugs");

            if (!resultSet.next()) return null;

            Drug drug = new Drug(resultSet);
            resultSet.close();

            return drug;
        }

        public static ArrayList<Drug> getAll() throws SQLException {
            ResultSet resultSet = Database.getAllDrugs();
            ArrayList<Drug> drugs = new ArrayList<Drug>();

            while (resultSet.next()) {
                Drug drug = new Drug(resultSet);
                drugs.add(drug);
            }
            resultSet.close();

            return drugs;
        }
    }
}