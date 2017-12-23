export const environment = {
  production: true,
  url: 'http://stud.us-east-1.elasticbeanstalk.com/',
  APARTMENT_NAME: "apartmentName",
  NO_OF_ROOMS: "noOfRooms",
  VACANCIES: "vacancies",
  COST: "cost",
  GENDER: "gender",
  FBID: "fbId",
  NOTES: "notes",
  USER_ID: "userId",
  POST_ACCOMODATION: "createAccommodationAddFromFacebook",
  POST_ACCOMMODATION_INDV: "createAccommodationAdd",
  ON_CAMPUS: "On-Campus",
  APARTMENT_TYPE: 'apartmentType',
  APARTMENT_NAME2: 'ApartmentName',
  GENDER_SPINNER: 'gender',
  MALE: "Male",
  FIRST_NAME: "firstName",
  LAST_NAME: "lastName",
  POST: "post",
  GET: "get",
  PUT: "put",
  ACCESS_TOKEN: "access_token",
  INVALID_REQUEST: "invalid request",
  noOfRooms: ['1 bhk/1 bath', '1 bhk/2 bath', '2 bhk/1 bath', '2 bhk/2 bath',
    '3 bhk/1 bath', '3 bhk/2 bath', '3 bhk/3 bath', 'Shared', 'Other'],
  vacancies: ['1', '2', '3', '4', 'Other', 'Lease Transfer'],
  GENDER_CODES: [{ code: "Male", description: "Male" },
  { code: "Female", description: "Female" },
  { code: "Doesnt Matter", description: "Doesnt Matter" },
  ],
  apartmentTypes: [{
    code: "on",
    description: "On-Campus"
  },
  {
    code: "off",
    description: "Off-Campus"
  },
  {
    code: "dorms",
    description: "Dorms"
  }],
  leftSpinnerValues: [{
    code: "apartmentType",
    description: "Apartment Type"
  },
  {
    code: "apartmentName",
    description: "Apartment Name"
  },
  {
    code: "gender",
    description: "Gender"
  }],
  getUniversityDetailsForUser: 'universities/getUniversityDetailsForUser',
  accessToken: 'access_token',
  universityName: 'universityName',
  getFlashCards: 'getFlashCards',
  getSimpleSearchAdds: 'getSimpleSearchAdds',
  userSelectedUnivs: 'userSelectedUnivs',
  getAllApartmentsWithType: 'getAllApartmentsWithType',
  getAdvancedSearchAdds: 'getAdvancedSearchAdds'
};
