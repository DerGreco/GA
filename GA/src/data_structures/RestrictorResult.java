package data_structures;

public class RestrictorResult {
		
	private boolean _changed;
	private boolean[] _truncated;
	
	public RestrictorResult(boolean _changed, boolean[] _truncated) {
		super();
		this._changed = _changed;
		this._truncated = _truncated;
	}

	public boolean is_changed() {
		return _changed;
	}

	public boolean[] get_truncated() {
		return _truncated;
	}	
}
