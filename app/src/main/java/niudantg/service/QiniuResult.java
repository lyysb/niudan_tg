package niudantg.service;

public interface QiniuResult {
	// status 1上传成功，2上传失败
	public void OnQiniuResult(String img, int status);
}
