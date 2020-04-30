package com.example.eng_reviewer;

public class source_storage {
}

//////////////////////////////////////csv에서 파일 읽어서 이터레이션 돌리기
//        CSVReader reader = new CSVReader(new FileReader("/sdcard/main_data_test.csv"));
//        while ((record = reader.readNext()) != null){
//        }
//////////////////////////////////////
//        InputStreamReader is = new InputStreamReader(getResources().openRawResource(R.raw.main_data)); // R.raw에서 데이터 가져오기
//        BufferedReader reader = new BufferedReader(is);
//        CSVReader read = new CSVReader(reader);
//////////////////////////////////////
//        Button_success.setOnClickListener(new View.OnClickListener() {
//@Override
//public void onClick(View view) {
//                String text_input = EditText_practice.getText().toString();
//                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
//                intent.putExtra("input", text_input);
//                startActivity(intent);
//        }
//                });
//////////////////////////////////////
//    String str = TextView_eng_snt.getText().toString();
// 파일 생성
//        File saveFile = new File("/sdcard/camdata"); // 저장 경로
//        File saveFile = new File("mnt/sdcard/Android/camdata"); // 저장 경로
// 폴더 생성
//        if(!saveFile.exists()){ // 폴더 없을 경우
//            saveFile.mkdirs(); // 폴더 생성
//        }
//        try {
//            long now = System.currentTimeMillis(); // 현재시간 받아오기
//            Date date = new Date(now); // Date 객체 생성
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String nowTime = sdf.format(date);
//
//            BufferedWriter buf = new BufferedWriter(new FileWriter(saveFile+"/CarnumData.txt", true));
//            buf.append(nowTime + " "); // 날짜 쓰기
//            buf.append(str); // 파일 쓰기
//            buf.newLine(); // 개행
//            buf.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        EditText_practice.addTextChangedListener(new TextWatcher() { 텍스트가 바뀌면 그에 따라 호출되는 녀석들
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (charSequence != null){
//                    input_string = charSequence.toString();
//                }
//                Log.d("EditText_practice", input_string + "," + wanted_string);
//                Button_success.setEnabled(input_string.equals(wanted_string));
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//////////////////////////////////////
//    public static void save(String result) {
//    String sdPath; //SD 카드의 경로
//    String externalState = Environment.getExternalStorageState();
//    if (externalState.equals(Environment.MEDIA_MOUNTED)) {
//        //외부 저장 장치가 마운트 되어서 읽어올 준비가 되었을 때
//        sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
//    }
//    else {
//        //마운트 되지 않았을 때
//         sdPath = Environment.MEDIA_UNMOUNTED;
//    }
//    File file = new File(sdPath + "/myDir");
//    if (!file.isDirectory()) file.mkdir();
//    //디렉토리 만들기
//         File file1 = new File(sdPath + "/mydir/text.txt");
//         try {
//             FileOutputStream fos = new FileOutputStream(file1);
//             fos.write(result.getBytes()); fos.close();
//         } catch (Exception e) {
//             Log.i("파일 저장 실패:", e.getMessage());
//         }
//}
//

////////////////////////////////////////////////