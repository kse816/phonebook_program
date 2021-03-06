package studentProgram;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentMain {
	//메뉴 : 성적 입력, 개별 성적 조회, 전체 성적 조회, 성적순 정렬, 성적 수정, 성적 삭제, 종료
	public static final int INSERT=1, SEARCH=2, PRINT=3, SORT=4, UPDATE=5, DELETE=6, EXIT=7;
	
	//스캐너 생성
	public static Scanner scan = new Scanner(System.in);
	
	public static void main(String[] args) {
		int menu = 0;
		boolean flag = false;
		
		//메뉴 선택 : 성적 입력, 개별 성적 조회, 전체 성적 조회, 성적순 정렬, 성적 수정, 성적 삭제, 종료
		while(!flag) {
			//메뉴 선택 함수 호출
			menu = selectMenu();
			switch(menu) {
			case INSERT: studentInsert(); break;
			case SEARCH: studentSearch(); break;
			case PRINT: studentPrint(); break;
			case SORT: studentSort(); break;
			case UPDATE: studentUpdate(); break;
			case DELETE: studentDelete(); break;
			case EXIT: System.out.println("성적 프로그램을 종료합니다.");	flag = true; break;
			}
		}
	}
	
	//메뉴 선택
	private static int selectMenu() {
		boolean flag = false;
		int menu = 0;
		
		while(!flag) {
			System.out.println("**************************************************************************");
			System.out.println("   1. 입력 2. 성적 조회 3. 전체 성적 조회  4. 정렬 5. 수정 6. 삭제  7. 종료");
			System.out.println("**************************************************************************");
			System.out.print("메뉴 선택 >> ");
			try {
				menu = Integer.parseInt(scan.nextLine());
			} catch(InputMismatchException e) {
				System.out.println("숫자를 입력해주세요.");
				continue;
			} catch(Exception e) {
				System.out.println("숫자를 입력해주세요.");
				continue;
			}
			
			if(menu>0 && menu<8) flag = true;
			else System.out.println("1~7 사이의 숫자를 입력해주세요.");
		}
		return menu;
	}
	
	//성적 데이터 입력
	private static void studentInsert() {
		//멤버변수 : 학번, 이름, 성별, 국어, 영어, 수학, 총점, 평균, 등급
		String id = null;
		String name = null;
		String gender = null;
		int kor = 0;
		int eng = 0;
		int math = 0;
		int total = 0;
		double avr = 0.0;
		char grade = '\u0000';
		
		//학번 입력 함수 호출
		while(true) {
			System.out.print("ID를 입력해 주세요. : ");
			id = scan.nextLine();

			if(id.length()>=1 && id.length() <= 10) {break;}
			else System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
		}
		
		
		//데이터 전체 조회
		List<StudentModel> list = new ArrayList<StudentModel>();
		list = DBController.dataSelect();
		
		//id 중복 확인
		if(list.size()>0) {
			for(StudentModel data : list) {
				if(id==data.getId()) {
					System.out.println("이미 입력된 id입니다.");
					return;
				}
			}
		}
		
		//이름 입력
		while(true) {
			System.out.print("이름을 입력해 주세요. : ");
			name = scan.nextLine();
			
			Pattern pattern = Pattern.compile("^[가-힣]*$");
			Matcher matcher = pattern.matcher(name);
			
			if(matcher.matches()) break;
			else System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
		}
		
		//성별 입력
		while(true) {
			System.out.print("성별을 입력해 주세요.(남성/여성) : ");
			gender = scan.nextLine();
			
			if(gender.equals("남성")||gender.equals("여성")) break;
			else System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
		}
		
		//국어, 영어, 수학 성적 입력 
		while(true) {
			System.out.print("국어점수를 입력해 주세요. : ");
			kor = scan.nextInt();
			
			if(kor >= 0 && kor <= 100) break;
			else System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
		}
		while(true) {
			System.out.print("영어점수를 입력해 주세요. : ");
			eng = scan.nextInt();
			
			if(kor >= 0 && kor <= 100) break;
			else System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
		}
		while(true) {
			System.out.print("수학점수를 입력해 주세요. : ");
			math = scan.nextInt();
			
			if(kor >= 0 && kor <= 100) break;
			else System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
		}
		
		//StudentData 객체 생성
		StudentModel sd = new StudentModel(id, name, gender, kor, eng, math);
		
		//총합, 평균, 등급 계산 함수 호출(StudentData)
		total = sd.calTotal();
		sd.setTotal(total);
		avr = sd.calAvr();
		sd.setAvr(avr);
		grade = sd.calGrade(avr);
		sd.setGrade(grade);
		
		//데이터베이스에 저장 
		int result = DBController.dataInsert(sd);
		
		if(result!=0) System.out.println(name+"님의 성적 입력이 완료되었습니다.");
		else System.out.println(name+"님의 성적 입력에 실패하였습니다.");
	}
	
	
	
	//개별 성적 조회
	private static void studentSearch() {
		String name = null;
		String searchData = null;
		
			while(true) {
				System.out.println("검색할 이름을 입력해 주세요.");
				System.out.print(">> ");
				name = scan.nextLine();
				
				Pattern pattern = Pattern.compile("^[가-힣]*$");
				Matcher matcher = pattern.matcher(name);
				
				if(matcher.matches()) break;
				else System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
			}
			searchData = name;
			
		
		//데이터베이스 조회 : 개별 데이터
		List<StudentModel> list = new ArrayList<StudentModel>();
		list = DBController.dataSearch(searchData, 1);

		//조회 결과 출력
		for(StudentModel data : list) {
			System.out.println(data);
		}
}

	
	//전체 성적 조회 
	private static void studentPrint() {
		List<StudentModel> list = new ArrayList<StudentModel>();
		
		//데이터베이스 조회 : 전체 데이터
		list = DBController.dataSelect();
		
		if(list.size()<=0) {
			System.out.println("입력된 데이터가 없습니다.");
			return;
		}
		
		//데이터 출력
		dataPrint(list);
		
		//전체 학생수, 전체 총점, 전체 평균 계산, 과목별 평균
		int count = list.size();
		int sum = 0;
		double totalAvr = 0.0;
		double korAvr = 0.0;
		double engAvr = 0.0;
		double mathAvr = 0.0;
		
		for(StudentModel data : list) {
			sum += data.getTotal();
			totalAvr += data.getAvr();
			korAvr += data.getKor();
			engAvr += data.getEng();
			mathAvr += data.getMath();
		}
		
		totalAvr /= (double)count;
		korAvr /= (double)count;
		engAvr /= (double)count;
		mathAvr /= (double)count;
		
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("전체 학생수 :"+count +"명 \t 전체 총점 : "+sum+"점 \t 전체 평균 : "+ String.format("%.2f", totalAvr)+"점");
		System.out.println("국어 평균 : "+String.format("%.2f", korAvr)+"점 \t 영어 평균 : "+String.format("%.2f", engAvr) +"점 \t 수학 평균 : "+String.format("%.2f", mathAvr)+"점");
		System.out.println("");
	}
	
	//성적 정렬 : 학번순, 이름순, 성적순
	private static void studentSort() {
		//조회 메뉴 선택
		int menu = sortMenu();
		
		if(menu==4) {
			System.out.println("취소합니다.");
			return;
		}

		//데이터베이스 조회 : 데이터 정렬
		List<StudentModel> list = new ArrayList<StudentModel>();
		list = DBController.dataSort(menu);
		
		if(list.size()<=0) {
			System.out.println("데이터가 없습니다.");
			return;
		}
		
		//정렬된 성적 출력
		dataPrint(list);
	}
	
	//성적 정렬 메뉴 선택 : 학번, 이름, 총점
	private static int sortMenu() {
		boolean flag = false;
		int menu = 0;
		
		while(!flag) {
			System.out.println("**************************************************");
			System.out.println("   1.이름순 정렬   2. 성적순 정렬   3. 취소");
			System.out.println("**************************************************");
			System.out.print("메뉴 선택 >> ");
			try {
				menu = Integer.parseInt(scan.nextLine());
			} catch(InputMismatchException e) {
				System.out.println("숫자를 입력해주세요.");
				continue;
			} catch(Exception e) {
				System.out.println("숫자를 입력해주세요.");
				continue;
			}
			
			if(menu>0 && menu<5) flag = true;
			else System.out.println("1~4 사이의 숫자를 입력해주세요.");
		}
		
		return menu;
	}
	
	//데이터 출력
	private static void dataPrint(List<StudentModel> list) {
		System.out.println("----------------------------------------------------------------------");
		System.out.println("학번\t"+"이름\t"+"성별\t"+" 국어\t"+" 영어\t"+" 수학\t"+" 총점\t"+"  평균\t"+" 등급");
		System.out.println("----------------------------------------------------------------------");
		for(StudentModel data : list) System.out.println(data);
	}
	
	//성적 수정 : ID로 조회, 과목(국,영,수) 선택하여 수정
	private static void studentUpdate() {
		final int KOR=1, ENG=2, MATH=3, EXIT=4;
		String name = null;
		int menu = 0;
		int kor = 0;
		int eng = 0;
		int math = 0;
		int result = 0;
		
		//학번 입력
		System.out.print("수정할 이름을 입력하세요 : ");
		name = scan.nextLine();
		
		//수정 전 데이터 확인 : 학번으로 데이터 조회
		List<StudentModel> list = new ArrayList<StudentModel>();
		list = DBController.dataSearch(String.valueOf(name), 1);
		
		if(list.size()<=0) {
			System.out.println("입력된 데이터가 없습니다.");
			return;
		}
		
		System.out.println("현재 성적은 다음과 같습니다.");
		dataPrint(list);
		
		//StudentData 객체로 저장
		StudentModel sd = list.get(0);
		
		//수정할 성적 선택
		menu = updateMenu();
		
		switch(menu) {
		case KOR : 
			System.out.print("수정할 국어점수를 입력하세요 : ");
			kor = scan.nextInt();
			sd.setKor(kor); break;
		case ENG : 
			System.out.print("수정할 영어점수를 입력하세요 : ");
			eng = scan.nextInt();
			sd.setEng(eng);	break;
		case MATH : 
			System.out.print("수정할 수학점수를 입력하세요 : ");
			math = scan.nextInt();
			sd.setMath(math); break;
		case EXIT : 
			System.out.println("수정을 취소합니다.");
			return;
		}
		
		//총점, 평균, 등급 계산
		int total = sd.calTotal();
		sd.setTotal(total);
		double avr = sd.calAvr();
		sd.setAvr(avr);
		char grade = sd.calGrade(avr);
		sd.setGrade(grade);
		
		//데이터베이스 수정
		result = DBController.dataUpdate(sd, menu);
		
		if(result!=0) System.out.println("성적 수정이 완료되었습니다.");
		else System.out.println("성적 수정에 실패하였습니다.");
	}
	
	//수정할 과목 선택
	private static int updateMenu() {
		boolean flag = false;
		int menu = 0;
		
		while(!flag) {
			System.out.println("**************************************************");
			System.out.println("   1. 국어수정 2. 영어수정 3. 수학수정  4. 취소");
			System.out.println("**************************************************");
			System.out.print("메뉴 선택 >> ");
			try {
				menu = Integer.parseInt(scan.nextLine());
			} catch(InputMismatchException e) {
				System.out.println("숫자를 입력해주세요.");
				continue;
			} catch(Exception e) {
				System.out.println("숫자를 입력해주세요.");
				continue;
			}
			
			if(menu>0 && menu<5) flag = true;
			else System.out.println("1~4 사이의 숫자를 입력해주세요.");
		}
		
		return menu;
	}

	//성적 삭제 : ID로 검색
	private static void studentDelete() {
		String id = null;
		int result = 0;
		
		//학번 입력
		System.out.print("삭제할 ID를 입력하세요 : ");
		id = scan.nextLine();
		
		//데이터베이스 삭제
		result = DBController.dataDelete(id);

		if(result!=0) System.out.println("성적 삭제가 완료되었습니다.");
		else System.out.println("성적 삭제에 실패하였습니다.");
	}
}
