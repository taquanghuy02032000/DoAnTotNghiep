package online.javalab.poly.utils;

public class Define {

    public static class Status{
        public static final int STATUS_NOT_STARTED = -1;
        public static final int STATUS_LEARNING = 0;
        public static final int STATUS_COMPLETED = 1;
        public static final int STATUS_NOT_ACTIVATED = 0;
        public static final int STATUS_ACTIVATED = 1;

    }
    public static class QuizStatus{
        public static final int STATUS_IS_DONE = 1;
        public static final int STATUS_IS_DOING= 0;
    }

    public static class TopicType{
        public static final int TYPE_PRIMARY = 0;
        public static final int TYPE_SUB = 1;
    }

    public static class BundleKeys{
        public static final String KEY_DATA = "bundle_data";
        public static final String KEY_LESSON = "bundle_lesson";
        public static final String KEY_PROGRESS = "bundle_progress";
        public static final String KEY_LESSON_RETURN = "bundle_lesson_return";
        public static final String KEY_IS_UPDATE = "bundle_is_update";
        public static final String KEY_IS_QA = "bundle_is_qa";
    }


    public static class Answers{
        public static final String ANS_CORRECTED = "Corrected";
        public static final String ANS_INCORRECT = "Incorrect";
    }


    public static final long DEFAULT_TIMEOUT = 60L;
    public static final long CLICK_TIME_INTERVAL = 300L;

    public static class Api {
        public static  final String BASE_URL = "https://javalabspoly.herokuapp.com";
        public static final String CONTENT_TYPE = "Content-Type: application/json";
        public static final String LOGIN_URL = "app_api/v1/auth/user";


        public static class BaseResponse {
            public static final String SUCCESS = "success";
            public static final String DATA = "data";
            public static final String PAGE = "page";
            public static final String ERROR = "error";
            public static final String ERROR_CODE = "error_code";
            public static final String ERROR_MESSAGE = "error_message";
        }

        public static class Error {
            // Declare variable name by function description
            public static final String NO_NETWORK_CONNECTION = "NO_NETWORK_CONNECTION";
            public static final String TIME_OUT = "TIME_OUT";
            public static final String UNKNOWN = "UNKNOWN";
        }

        public static class Key {
            public static final String AUTHORIZATION = "Authorization";
            public static final String LOGIN_NAME = "login_name";
            public static final String PASSWORD = "password";
            public static final String TOKEN = "token";

        }

        public static class Http {
            public static final int RESPONSE_CODE_ACCESS_TOKEN_EXPIRED = 403;
        }
    }

    public static class StorageKey{
        public static final String USER_ID = "user_id";
        public static final String USER_NAME = "user_name";
        public static final String IS_CHECK_NOTIFICATION = "IS_CHECK_NOTIFICATION";
    }

    public static class ResponseStatus {
        public static final int LOADING = 1;
        public static final int SUCCESS = 2;
        public static final int ERROR = 0;
    }

    public static class ButtonConstants{
        public static final String START = "GET STARTED";
        public static final String CONTINUES = "CONTINUE";
        public static final String REVIEW = "REVIEW";
        public static final String START_QUIZ = "START QUIZ";
        public static final String FINISH = "FINISH";
    }

    public static class Message{
        public static final String MES_FINISH_QUIZ_1 = "Amazing...Good Job!";
        public static final String MES_FINISH_QUIZ_2 = "Congratulations!...You passed the test.";
        public static final String MES_FINISH_QUIZ_3 = "Don't worry... Let's try again.";
        public static final String ASK_TO_CONNECT = "Please connect to the internet to continue";
        public static final String NO_CONNECT = "No internet connection";

    }


    public  static class Tab{
        public static final int TAB_ID_ALL = 0;
        public static final int TAB_ID_NOT_START = 1;
        public static final int TAB_ID_LEARNING = 2;
        public static final int TAB_ID_DONE = 3;
    }

}
