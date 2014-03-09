package flash.light.pro;

import android.app.IntentService;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;

public class FlashlightIntentService extends IntentService {

	public FlashlightIntentService() {
		super("FlashlightIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Camera camera = Camera.open();
		Parameters parameters = camera.getParameters();
		parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
		camera.setParameters(parameters);
		camera.startPreview();
	}

}
