// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --env:prod` then `environment.prod.ts` will be used instead.
// The list of which env maps to which file can be found in `.angular-cli.json`.

export const environment = {
  url: 'http://localhost:8080/',
  production: false,
  appId: '931333680308184',
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
  accessToken: 'accessToken',
  universityName: 'universityName',
  getFlashCards: 'getFlashCards',
  getSimpleSearchAdds: 'getSimpleSearchAdds',
  userSelectedUnivs: 'userSelectedUnivs',
  getAllApartmentsWithType: 'getAllApartmentsWithType',
  getAdvancedSearchAdds: 'getAdvancedSearchAdds',
  noOfRooms: [{ code: "	1 bhk/1 bath	", description: "	1 bhk/1 bath	" },
  { code: "1 bhk/2 bath", description: "1 bhk/2 bath" },
  { code: "2 bhk/1 bath", description: "2 bhk/1 bath" },
  { code: "2 bhk/2 bath", description: "2 bhk/2 bath" },
  { code: "3 bhk/1 bath", description: "3 bhk/1 bath" },
  { code: "3 bhk/2 bath", description: "3 bhk/2 bath" },
  { code: "3 bhk/3 bath", description: "3 bhk/3 bath" },
  { code: "Shared	", description: "Shared" },
  { code: "Other	", description: "Other" }],
  vacancies:
    [{ code: "1", description: "1" },
    { code: "2", description: "2" },
    { code: "3", description: "3" },
    { code: "4", description: "4" },
    { code: "Other", description: "Other" },
    { code: "Lease Transfer", description: "Lease Transfer" }],
  createUser: 'profile/createUser',
  connected: 'connected',
  getUserUniversities: 'profile/getUserUniversities',
  getNotificationSettings: 'profile/getNotificationSettings',
  subscribeNotifications: 'profile/subscribeNotifications',
  all: 'all',
  createAccommodationAdd: 'profile/createAccommodationAdd',
  cloudinaryURL: 'https://api.cloudinary.com/v1_1/duf1ntj7z/upload',
  CLOUDINARY_PRESET_VALUE: 'qdisf7f1',
  upload_preset: 'upload_preset',
  file: 'file',
  success: 'success',
  getUserPosts: 'profile/getUserPosts',
  login: 'Login',
  logout: 'Logout'
}
