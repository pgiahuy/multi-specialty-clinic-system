import { useMemo } from "react";

const sortByTime = (a, b) => {
    const timeA = `${a.shiftStartTime || ""}${a.shiftEndTime || ""}`;
    const timeB = `${b.shiftStartTime || ""}${b.shiftEndTime || ""}`;
    return timeA.localeCompare(timeB);
};

const getSessionRank = (name) => {
    const order = ["Sáng", "Chiều"];
    if (!name) return order.length;
    for (let i = 0; i < order.length; i++) {
        if (name.includes(order[i])) return i;
    }
    return order.length;
};

const ScheduleBooking = ({ schedules, loading, hasAnyFilter, selectedDate, selectedScheduleId, onSelectSchedule }) => {
    const groupedSchedules = useMemo(() => {
        const doctorDateMap = new Map();

        (schedules || []).forEach((schedule) => {
            const doctorName = schedule.doctorName || "Bác sĩ chưa cập nhật";
            const date = schedule.date || "Chưa cập nhật ngày";
            const specialty = schedule.specialtyName || "Khoa chưa cập nhật";
            const session = schedule.session || "Buổi chưa cập nhật";
            const groupKey = `${doctorName}__${date}`;

            if (!doctorDateMap.has(groupKey)) {
                doctorDateMap.set(groupKey, {
                    doctorName,
                    date,
                    specialties: new Map(),
                });
            }

            const doctorDateGroup = doctorDateMap.get(groupKey);
            if (!doctorDateGroup.specialties.has(specialty)) {
                doctorDateGroup.specialties.set(specialty, new Map());
            }

            const specialtyMap = doctorDateGroup.specialties.get(specialty);
            if (!specialtyMap.has(session)) {
                specialtyMap.set(session, []);
            }

            specialtyMap.get(session).push(schedule);
        });

        return Array.from(doctorDateMap.values())
            .sort((a, b) => {
                const byDoctor = a.doctorName.localeCompare(b.doctorName);
                if (byDoctor !== 0) {
                    return byDoctor;
                }
                return a.date.localeCompare(b.date);
            })
            .map((doctorDateGroup) => ({
                ...doctorDateGroup,
                specialties: Array.from(doctorDateGroup.specialties.entries())
                    .sort((a, b) => a[0].localeCompare(b[0]))
                    .map(([specialtyName, sessionMap]) => ({
                        specialtyName,
                        sessions: Array.from(sessionMap.entries())
                            .sort((a, b) => {
                                const ra = getSessionRank(a[0]);
                                const rb = getSessionRank(b[0]);
                                if (ra !== rb) return ra - rb;
                                return a[0].localeCompare(b[0]);
                            })
                            .map(([sessionName, sessionSchedules]) => ({
                                sessionName,
                                schedules: sessionSchedules.sort(sortByTime),
                            })),
                    })),
            }));
    }, [schedules]);

    if (loading) {
        return <div className="text-muted small">Đang tải lịch khám...</div>;
    }

    if (!hasAnyFilter) {
        return <div className="text-muted small fst-italic">Vui lòng chọn ít nhất thông tin.</div>;
    }

    if (!groupedSchedules.length) {
        return (
            <div className="text-muted small fst-italic">
                {selectedDate
                    ? "Không có lịch khám phù hợp."
                    : "Chưa có lịch phù hợp. Bạn có thể thêm Ngày khám để lọc chính xác hơn."}
            </div>
        );
    }

    return (
        <div className="d-flex flex-column gap-3">
            {groupedSchedules.map((doctorDateGroup) => {
                return (
                    <div key={`${doctorDateGroup.doctorName}-${doctorDateGroup.date}`} className="border rounded-4 p-3" style={{ backgroundColor: "#fafcff" }}>
                        <div className="d-flex justify-content-between align-items-center mb-2">
                            <div className="fw-semibold text-primary">Bác sĩ: {doctorDateGroup.doctorName}</div>
                            <div className="small text-primary">Ngày: {doctorDateGroup.date}</div>
                        </div>

                        <div className="d-flex flex-column gap-3">
                            {doctorDateGroup.specialties.map((specialtyGroup) => {
                                return (
                                    <div key={`${doctorDateGroup.doctorName}-${doctorDateGroup.date}-${specialtyGroup.specialtyName}`} className="border rounded-3 p-2 bg-white">
                                        <div className="small fw-semibold text-dark mb-2">{specialtyGroup.specialtyName}</div>

                                        {specialtyGroup.sessions.map((sessionGroup) => {
                                            return (
                                                <div key={`${doctorDateGroup.doctorName}-${doctorDateGroup.date}-${specialtyGroup.specialtyName}-${sessionGroup.sessionName}`}
                                                    className="mb-3 border rounded-3 p-2"
                                                    style={{ backgroundColor: sessionGroup.sessionName === "Sáng" ? "#f0f8ff" : "#fff8f0" }}>

                                                    <div className="small fw-semibold text-dark mb-1">Buổi {sessionGroup.sessionName}</div>

                                                    <div className="d-flex flex-wrap gap-2">
                                                        {sessionGroup.schedules.map((schedule) => {
                                                            const timeLabel = `${schedule.shiftStartTime || ""}${schedule.shiftStartTime && schedule.shiftEndTime ? " - " : ""}${schedule.shiftEndTime || ""}`;
                                                            const selected = String(selectedScheduleId) === String(schedule.id);
                                                            const remainingSlots = (schedule.maxPatients || 0) - (schedule.currentPatients || 0);
                                                            const isFull = remainingSlots <= 0;
                                                            const specialtyLabel = schedule.specialtyName || "Khoa chưa cập nhật";

                                                            return (
                                                                <div key={schedule.id}>
                                                                    <button
                                                                        type="button"
                                                                        title={isFull ? "Hết chỗ" : "Còn chỗ"}
                                                                        className={`text-start p-2 px-3 rounded-3 border ${selected
                                                                            ? "border-success shadow-sm"
                                                                            : sessionGroup.sessionName === 'Sáng'
                                                                                ? "border-primary"
                                                                                : "border-warning"
                                                                            }`} onClick={() => !isFull && onSelectSchedule(schedule.id)}
                                                                        disabled={isFull}
                                                                        style={{ transition: "all 0.2s ease", opacity: isFull ? 0.55 : 1, backgroundColor: selected ? "#e6f4ea" : "#ffffff" }}
                                                                    >
                                                                        {timeLabel || "Chưa có thời gian"}
                                                                    </button>
                                                                </div>
                                                            );
                                                        })}
                                                    </div>
                                                </div>
                                            );
                                        })}
                                    </div>
                                );
                            })}
                        </div>
                    </div>
                );
            })}
        </div >
    );
};

export default ScheduleBooking;
