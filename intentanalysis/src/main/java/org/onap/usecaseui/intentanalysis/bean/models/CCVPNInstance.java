package org.onap.usecaseui.intentanalysis.bean.models;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="ccvpn_instance")
@Data

public class CCVPNInstance implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "instance_id")
    private String instanceId;

    @Column(name = "job_id")
    private String jobId;

    @Column(name = "progress")
    private int progress;

    @Column(name = "status")
    private String status;

    @Column(name = "resource_instance_id")
    private String resourceInstanceId;

    @Column(name = "name")
    private String name;

    @Column(name = "cloud_point_name")
    private String cloudPointName;

    @Column(name = "access_point_one_name")
    private String accessPointOneName;

    @Column(name = "access_point_one_band_width")
    private int accessPointOneBandWidth;

    @Column(name = "line_num")
    private String lineNum;

    @Column(name = "protect_status")
    private int protectStatus;

    @Column(name = "delete_state")
    private int deleteState;

    @Column(name = "protection_cloud_point_name")
    private String protectionCloudPointName;

    @Column(name = "protection_type")
    private String protectionType;
}
