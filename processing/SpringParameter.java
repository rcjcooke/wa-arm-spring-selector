/**
 * 
 */
package wa.arm.springselector;

import java.util.ArrayList;

public class SpringParameter {
	private String mParameterName;
	private float mMin;
	private float mMax;
	private ArrayList<String> mListOfStringParameterOptions = new ArrayList<String>();
	private ArrayList<String> mListOfSelectedParameterOptions = new ArrayList<String>();
	private float mSelectionMin;
	private float mSelectionMax;
	private float[] mCurrentRange = new float[2];
	private char mParameterType;

	SpringParameter(String Name) {
		mParameterName = Name;
	}

	void setListOfStringParameterOptions(String[] ParameterOptions) {
		for (String s : ParameterOptions) {
			if (!mListOfStringParameterOptions.contains(s)) {
				mListOfStringParameterOptions.add(s);
			}
		}
	}

	boolean isInMyRange(float ValueToTest) {
		return ((ValueToTest >= mCurrentRange[0] & ValueToTest <= mCurrentRange[1]));// ||((ValueToTest>=CurrentRange[0]/2)
																					// &
																					// (ValueToTest<=CurrentRange[1]/2))||((ValueToTest>=CurrentRange[0]/4)
																					// &
																					// (ValueToTest<=CurrentRange[1]/4))||((ValueToTest>=CurrentRange[0]/6)
																					// &
																					// (ValueToTest<=CurrentRange[1]/6))||((ValueToTest>=CurrentRange[0]/8)
																					// &
																					// (ValueToTest<=CurrentRange[1]/8)));
																					// //||((ValueToTest>=CurrentRange[0]/2)
																					// &
																					// (ValueToTest<=CurrentRange[1]/2))
	}

	boolean isInMySelectionList(String StringToTest) {
		return (mListOfSelectedParameterOptions.contains(StringToTest));
	}

	void setTypeFloat() {
		mParameterType = 'F';
	}

	void setTypeString() {
		mParameterType = 'S';
	}

	boolean isTypeString() {
		return (mParameterType == 'S');
	}

	boolean isTypeFloat() {
		return (mParameterType == 'F');
	}

	String getParameterName() {
		return mParameterName;
	}
}
