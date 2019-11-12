using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class RubyController : MonoBehaviour
{
    // Start is called before the first frame update
    Rigidbody2D rigidbody2d;
    void Start()
    {
        QualitySettings.vSyncCount = 0;
        Application.targetFrameRate = 25;
        rigidbody2d = GetComponent<Rigidbody2D>();
    }

    // Update is called once per frame
    void Update()
    {
        float horizontal = Input.GetAxis("Horizontal");
        float vertical = Input.GetAxis("Vertical");

        Debug.Log(horizontal);
        Vector2 position = transform.position;
        position.x = position.x + 0.1f * horizontal;
        position.y = position.y + 0.1f * vertical;
        transform.position = position;

        rigidbody2d.MovePosition(position);
    }
}
